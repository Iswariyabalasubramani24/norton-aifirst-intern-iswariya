package com.iswariya.scammessagedetector.data.mapper

import com.google.gson.JsonParser
import com.iswariya.scammessagedetector.domain.model.AnalysisResult
import com.iswariya.scammessagedetector.domain.model.RiskLevel

// Converts the raw AI text response into a structured domain AnalysisResult.
// Defensive by design — every field has a safe fallback for malformed output.
fun parseAnalysisResult(raw: String): AnalysisResult {
    val cleaned = raw.trim()
        .removePrefix("```json").removePrefix("```").removeSuffix("```").trim()

    val obj = JsonParser.parseString(cleaned).asJsonObject

    val riskLevel = when (obj.get("riskLevel")?.asString?.lowercase()) {
        "safe" -> RiskLevel.SAFE
        "dangerous" -> RiskLevel.DANGEROUS
        else -> RiskLevel.SUSPICIOUS
    }
    val confidence = obj.get("confidence")?.asInt ?: 50
    val explanation = obj.get("explanation")?.asString ?: "Unable to determine risk level."

    return AnalysisResult(riskLevel, confidence, explanation)
}
