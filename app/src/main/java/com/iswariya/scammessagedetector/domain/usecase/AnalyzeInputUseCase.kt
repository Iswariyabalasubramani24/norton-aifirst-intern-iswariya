package com.iswariya.scammessagedetector.domain.usecase

import com.iswariya.scammessagedetector.domain.model.AnalysisResult
import com.iswariya.scammessagedetector.domain.repository.ScanRepository
import javax.inject.Inject

// Encapsulates the single business action: analyze a message for scam signals.
// ViewModels call this class directly, keeping all business logic out of the UI layer.
class AnalyzeInputUseCase @Inject constructor(private val repository: ScanRepository) {
    suspend operator fun invoke(input: String): Result<AnalysisResult> = repository.analyze(input)
}

