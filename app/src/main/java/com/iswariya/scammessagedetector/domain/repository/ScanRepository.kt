package com.iswariya.scammessagedetector.domain.repository

import com.iswariya.scammessagedetector.domain.model.AnalysisResult

// Contract between the domain layer and the data layer.
// The data layer provides a concrete implementation; the domain layer never knows about it.
interface ScanRepository {
    suspend fun analyze(input: String): Result<AnalysisResult>
}
