package com.iswariya.scammessagedetector.domain.model

// Represents the threat classification of a scanned message.
// Used across all layers (domain → data → UI) as the single source of truth for risk.
enum class RiskLevel {
    SAFE, SUSPICIOUS, DANGEROUS;

    val label: String
        get() = when (this) {
            SAFE -> "Safe"
            SUSPICIOUS -> "Suspicious"
            DANGEROUS -> "Dangerous"
        }
}
