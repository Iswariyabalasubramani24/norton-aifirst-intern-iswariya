package com.iswariya.scammessagedetector.domain.model

// Pure domain model — no Android or framework dependencies.
// Carries the complete verdict produced by the AI analysis pipeline.
data class AnalysisResult(
    val riskLevel: RiskLevel,
    // 0 = uncertain, 100 = fully confident
    val confidence: Int,
    val explanation: String
)
