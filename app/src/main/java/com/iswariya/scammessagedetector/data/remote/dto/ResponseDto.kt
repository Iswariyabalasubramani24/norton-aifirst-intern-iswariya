package com.iswariya.scammessagedetector.data.remote.dto

import com.google.gson.annotations.SerializedName

// A single candidate reply returned by the model
data class ChoiceDto(
    @SerializedName("message") val message: MessageDto
)

// Top-level response from the chat completion API;
// `choices` contains at least one entry on a successful response
data class AnalysisResponseDto(
    @SerializedName("choices") val choices: List<ChoiceDto>
)
