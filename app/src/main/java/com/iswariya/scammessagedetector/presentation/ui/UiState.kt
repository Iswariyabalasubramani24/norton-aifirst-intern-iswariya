package com.iswariya.scammessagedetector.presentation.ui

import com.iswariya.scammessagedetector.domain.model.AnalysisResult

data class UiState(
    val inputText: String = "",
    val isLoading: Boolean = false,
    val result: AnalysisResult? = null,
    val error: String? = null
)

val exampleScamMessages = listOf(
    "URGENT: Your Barclays account has been suspended due to suspicious activity. Verify your identity NOW to avoid permanent closure: http://barclays-secure-verify.xyz/login",
    "Congratulations! You've been selected as a national lottery winner. You've won £85,000! Send your bank details and a £50 processing fee to claims@lottery-winners.net",
    "Your DHL parcel could not be delivered. A £1.99 redelivery fee is required within 24hrs: dhl-redelivery-uk.com/pay?id=83921"
)