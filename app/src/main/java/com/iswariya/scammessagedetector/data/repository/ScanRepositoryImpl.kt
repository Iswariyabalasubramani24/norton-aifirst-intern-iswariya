package com.iswariya.scammessagedetector.data.repository

import com.iswariya.scammessagedetector.data.mapper.parseAnalysisResult
import com.iswariya.scammessagedetector.data.remote.api.GroqApiService
import com.iswariya.scammessagedetector.data.remote.dto.AnalysisRequestDto
import com.iswariya.scammessagedetector.data.remote.dto.MessageDto
import com.iswariya.scammessagedetector.domain.model.AnalysisResult
import com.iswariya.scammessagedetector.domain.repository.ScanRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class ScanRepositoryImpl @Inject constructor(private val api: GroqApiService) : ScanRepository {

    private val systemPrompt = """
        You are a scam and fraud detection AI. Analyze the provided message or URL and return ONLY valid JSON (no markdown, no backticks) with this exact structure:
{
  "riskLevel": "safe" | "suspicious" | "dangerous",
  "confidence": <integer 0-100>,
  "explanation": "<2-3 sentence plain-English explanation of why this is or isn't a scam>"
}

You MUST classify into exactly one of the three categories: "safe", "suspicious", or "dangerous".
Do NOT skip "suspicious". If the message is not clearly safe or clearly dangerous, you MUST classify it as "suspicious".

Decision rules:
- Use "dangerous" ONLY when there are strong and clear scam indicators such as:
  - phishing links or spoofed domains
  - requests for passwords, OTPs, or banking details
  - threats, urgency, or fear-based language (e.g., "account suspended", "act immediately")
  - prize scams or payment requests

- Use "safe" ONLY when the message is clearly legitimate:
  - known trusted domain (e.g., amazon.com, paypal.com)
  - no urgency, no threats, no credential requests
  - normal transactional or informational message

- Use "suspicious" in ALL uncertain or borderline cases, including:
  - unknown or slightly unusual domains
  - unsolicited messages
  - vague warnings or mild urgency
  - messages that cannot be confidently verified as safe
  - any case where you are unsure

STRICT RULE:
If confidence is below 80 OR the message cannot be clearly verified as safe or dangerous, you MUST return "suspicious".

Return ONLY the JSON object, nothing else.
    """.trimIndent()

    override suspend fun analyze(input: String): Result<AnalysisResult> {
        return try {
            val response = api.analyze(
                AnalysisRequestDto(
                    messages = listOf(
                        MessageDto("system", systemPrompt),
                        MessageDto("user", input)
                    )
                )
            )
            val raw = response.choices.firstOrNull()?.message?.content
                ?: return Result.failure(Exception("Empty response from model"))
            Result.success(parseAnalysisResult(raw))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

