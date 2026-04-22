package com.iswariya.scammessagedetector.presentation.viewmodel

import com.iswariya.scammessagedetector.domain.model.AnalysisResult
import com.iswariya.scammessagedetector.domain.model.RiskLevel
import com.iswariya.scammessagedetector.domain.repository.ScanRepository
import com.iswariya.scammessagedetector.domain.usecase.AnalyzeInputUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
//AI Generated Test Case
class ScanViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ScanViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ScanViewModel(fakeUseCase())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ---- initial state ----

    @Test
    fun `initial state has empty input, not loading, no result, no error`() {
        val state = viewModel.uiState.value
        assertEquals("", state.inputText)
        assertFalse(state.isLoading)
        assertNull(state.result)
        assertNull(state.error)
    }

    // ---- onInputChanged ----

    @Test
    fun `onInputChanged updates inputText`() {
        viewModel.onInputChanged("hello scam")
        assertEquals("hello scam", viewModel.uiState.value.inputText)
    }

    @Test
    fun `onInputChanged clears result and error from a previous analysis`() = runTest {
        viewModel = ScanViewModel(fakeUseCase(Result.success(safeResult())))
        viewModel.onInputChanged("first message")
        viewModel.onAnalyze()

        viewModel.onInputChanged("edited message")

        val state = viewModel.uiState.value
        assertEquals("edited message", state.inputText)
        assertNull(state.result)
        assertNull(state.error)
    }

    @Test
    fun `onInputChanged clears error left by a failed analysis`() = runTest {
        viewModel = ScanViewModel(fakeUseCase(Result.failure(RuntimeException("boom"))))
        viewModel.onInputChanged("bad message")
        viewModel.onAnalyze()

        viewModel.onInputChanged("retry text")

        assertNull(viewModel.uiState.value.error)
    }

    // ---- onAnalyze – early-return cases ----

    @Test
    fun `onAnalyze does nothing when input is empty`() {
        viewModel.onInputChanged("")
        viewModel.onAnalyze()

        val state = viewModel.uiState.value
        assertEquals("", state.inputText)
        assertFalse(state.isLoading)
        assertNull(state.result)
        assertNull(state.error)
    }

    @Test
    fun `onAnalyze does nothing when input is whitespace only`() {
        viewModel.onInputChanged("   \t\n  ")
        viewModel.onAnalyze()

        val state = viewModel.uiState.value
        assertEquals("   \t\n  ", state.inputText)
        assertFalse(state.isLoading)
        assertNull(state.result)
        assertNull(state.error)
    }

    // ---- onAnalyze – success ----

    @Test
    fun `onAnalyze success stores result and clears loading and error`() = runTest {
        val expected = safeResult()
        viewModel = ScanViewModel(fakeUseCase(Result.success(expected)))

        viewModel.onInputChanged("Congratulations you have won")
        viewModel.onAnalyze()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(expected, state.result)
        assertNull(state.error)
    }

    @Test
    fun `onAnalyze success trims leading and trailing whitespace before calling use case`() =
        runTest {
            var capturedInput: String? = null
            viewModel = ScanViewModel(AnalyzeInputUseCase(object : ScanRepository {
                override suspend fun analyze(input: String): Result<AnalysisResult> {
                    capturedInput = input
                    return Result.success(safeResult())
                }
            }))

            viewModel.onInputChanged("  check this  ")
            viewModel.onAnalyze()

            assertEquals("check this", capturedInput)
        }

    // ---- onAnalyze – failure ----

    @Test
    fun `onAnalyze failure sets error to exception message and clears loading and result`() =
        runTest {
            viewModel =
                ScanViewModel(fakeUseCase(Result.failure(RuntimeException("Network timeout"))))

            viewModel.onInputChanged("some scam text")
            viewModel.onAnalyze()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertNull(state.result)
            assertEquals("Network timeout", state.error)
        }

    @Test
    fun `onAnalyze failure with null exception message falls back to default error text`() =
        runTest {
            viewModel = ScanViewModel(fakeUseCase(Result.failure(RuntimeException())))

            viewModel.onInputChanged("some scam text")
            viewModel.onAnalyze()

            assertEquals("Analysis failed. Please try again.", viewModel.uiState.value.error)
        }

    @Test
    fun `onAnalyze failure clears any previously stored result`() = runTest {
        val repo = FakeScanRepository(Result.success(safeResult()))
        viewModel = ScanViewModel(AnalyzeInputUseCase(repo))
        viewModel.onInputChanged("first")
        viewModel.onAnalyze()

        repo.response = Result.failure(RuntimeException("error"))
        viewModel.onInputChanged("second")
        viewModel.onAnalyze()

        assertNull(viewModel.uiState.value.result)
    }

    // ---- helpers ----

    private fun fakeUseCase(response: Result<AnalysisResult> = Result.success(safeResult())) =
        AnalyzeInputUseCase(FakeScanRepository(response))

    private fun safeResult() = AnalysisResult(
        riskLevel = RiskLevel.SAFE,
        confidence = 95,
        explanation = "No scam indicators detected."
    )
}

private class FakeScanRepository(var response: Result<AnalysisResult>) : ScanRepository {
    override suspend fun analyze(input: String): Result<AnalysisResult> = response
}
