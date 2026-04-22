package com.iswariya.scammessagedetector.data.remote.dto

import com.google.gson.annotations.SerializedName

// A single conversation turn; role is one of: "system", "user", or "assistant"
data class MessageDto(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

// Request body sent to the Groq / OpenAI-compatible chat completion endpoint
data class AnalysisRequestDto(
    @SerializedName("model") val model: String = "llama-3.3-70b-versatile",
    @SerializedName("max_tokens") val maxTokens: Int = 512,
    @SerializedName("messages") val messages: List<MessageDto>
)
