package com.iswariya.scammessagedetector.data.remote.api

import com.iswariya.scammessagedetector.data.remote.dto.AnalysisRequestDto
import com.iswariya.scammessagedetector.data.remote.dto.AnalysisResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface GroqApiService {
    @POST("openai/v1/chat/completions")
    suspend fun analyze(@Body request: AnalysisRequestDto): AnalysisResponseDto
}