package com.iswariya.scammessagedetector.model

import com.iswariya.scammessagedetector.domain.model.AnalysisResult
import com.iswariya.scammessagedetector.domain.model.RiskLevel
import com.iswariya.scammessagedetector.domain.repository.ScanRepository
import com.iswariya.scammessagedetector.domain.usecase.AnalyzeInputUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScanRepositoryTest {

    private fun fakeRepository(
        response: Result<AnalysisResult>,
        capturedInput: MutableList<String> = mutableListOf()
    ) = object : ScanRepository {
        override suspend fun analyze(input: String): Result<AnalysisResult> {
            capturedInput.add(input)
            return response
        }
    }

    // AnalyzeInputUseCase delegation

    @Test
    fun `use case returns success result from repository`() = runTest {
        val expected = AnalysisResult(RiskLevel.SAFE, 92, "Message appears legitimate.")
        val useCase = AnalyzeInputUseCase(fakeRepository(Result.success(expected)))

        val result = useCase("Hello, is this a scam?")

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `use case propagates failure from repository`() = runTest {
        val error = RuntimeException("Network timeout")
        val useCase = AnalyzeInputUseCase(fakeRepository(Result.failure(error)))

        val result = useCase("Win a free iPhone!")

        assertTrue(result.isFailure)
        assertEquals("Network timeout", result.exceptionOrNull()?.message)
    }

    @Test
    fun `use case passes input to repository unchanged`() = runTest {
        val captured = mutableListOf<String>()
        val useCase = AnalyzeInputUseCase(
            fakeRepository(
                Result.success(AnalysisResult(RiskLevel.SUSPICIOUS, 60, "Possibly fraudulent.")),
                captured
            )
        )
        val input = "Click here to claim your prize: http://scam.example.com"

        useCase(input)

        assertEquals(1, captured.size)
        assertEquals(input, captured[0])
    }

    @Test
    fun `use case delegates DANGEROUS result correctly`() = runTest {
        val expected = AnalysisResult(RiskLevel.DANGEROUS, 99, "Known phishing domain.")
        val useCase = AnalyzeInputUseCase(fakeRepository(Result.success(expected)))

        val result = useCase("Verify your bank account now")

        assertEquals(RiskLevel.DANGEROUS, result.getOrNull()?.riskLevel)
        assertEquals(99, result.getOrNull()?.confidence)
    }

    // RiskLevel domain model

    @Test
    fun `RiskLevel labels match expected display strings`() {
        assertEquals("Safe", RiskLevel.SAFE.label)
        assertEquals("Suspicious", RiskLevel.SUSPICIOUS.label)
        assertEquals("Dangerous", RiskLevel.DANGEROUS.label)
    }

    @Test
    fun `AnalysisResult holds all fields correctly`() {
        val result = AnalysisResult(
            riskLevel = RiskLevel.SUSPICIOUS,
            confidence = 72,
            explanation = "Sender domain does not match official organization."
        )

        assertEquals(RiskLevel.SUSPICIOUS, result.riskLevel)
        assertEquals(72, result.confidence)
        assertEquals("Sender domain does not match official organization.", result.explanation)
    }
}