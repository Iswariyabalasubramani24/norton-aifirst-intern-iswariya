package com.iswariya.scammessagedetector.network

import com.iswariya.scammessagedetector.data.mapper.parseAnalysisResult
import com.iswariya.scammessagedetector.domain.model.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Test

class ResponseParserTest {

    //Risk level mapping

    @Test
    fun `parseAnalysisResult maps 'safe' to SAFE risk level`() {
        val json =
            """{"riskLevel":"safe","confidence":90,"explanation":"This message appears legitimate."}"""

        val result = parseAnalysisResult(json)

        assertEquals(RiskLevel.SAFE, result.riskLevel)
    }

    @Test
    fun `parseAnalysisResult maps 'dangerous' to DANGEROUS risk level`() {
        val json =
            """{"riskLevel":"dangerous","confidence":97,"explanation":"This is a phishing scam."}"""

        val result = parseAnalysisResult(json)

        assertEquals(RiskLevel.DANGEROUS, result.riskLevel)
    }

    @Test
    fun `parseAnalysisResult maps unknown risk level to SUSPICIOUS`() {
        val json = """{"riskLevel":"unknown","confidence":60,"explanation":"Cannot determine."}"""

        val result = parseAnalysisResult(json)

        assertEquals(RiskLevel.SUSPICIOUS, result.riskLevel)
    }

    @Test
    fun `parseAnalysisResult defaults to SUSPICIOUS when riskLevel is missing`() {
        val json = """{"confidence":50,"explanation":"No risk level provided."}"""

        val result = parseAnalysisResult(json)

        assertEquals(RiskLevel.SUSPICIOUS, result.riskLevel)
    }

    // Markdown stripping

    @Test
    fun `parseAnalysisResult strips markdown json code block`() {
        val markdown = """
            ```json
            {"riskLevel":"safe","confidence":85,"explanation":"Looks legitimate."}
            ```
        """.trimIndent()

        val result = parseAnalysisResult(markdown)

        assertEquals(RiskLevel.SAFE, result.riskLevel)
        assertEquals(85, result.confidence)
    }

    @Test
    fun `parseAnalysisResult strips plain markdown code block without language tag`() {
        val markdown =
            "```\n{\"riskLevel\":\"dangerous\",\"confidence\":99,\"explanation\":\"Scam.\"}\n```"

        val result = parseAnalysisResult(markdown)

        assertEquals(RiskLevel.DANGEROUS, result.riskLevel)
        assertEquals(99, result.confidence)
    }

    // Confidence parsing

    @Test
    fun `parseAnalysisResult parses confidence as integer`() {
        val json =
            """{"riskLevel":"suspicious","confidence":73,"explanation":"Potentially fraudulent."}"""

        val result = parseAnalysisResult(json)

        assertEquals(73, result.confidence)
    }

    @Test
    fun `parseAnalysisResult defaults confidence to 50 when missing`() {
        val json = """{"riskLevel":"safe","explanation":"No confidence field."}"""

        val result = parseAnalysisResult(json)

        assertEquals(50, result.confidence)
    }

    // Explanation parsing

    @Test
    fun `parseAnalysisResult parses explanation string`() {
        val explanation = "This URL redirects to a known phishing site. Do not click."
        val json = """{"riskLevel":"dangerous","confidence":98,"explanation":"$explanation"}"""

        val result = parseAnalysisResult(json)

        assertEquals(explanation, result.explanation)
    }

    @Test
    fun `parseAnalysisResult defaults explanation when missing`() {
        val json = """{"riskLevel":"safe","confidence":80}"""

        val result = parseAnalysisResult(json)

        assertEquals("Unable to determine risk level.", result.explanation)
    }

    // Case insensitivity

    @Test
    fun `parseAnalysisResult maps risk level case-insensitively`() {
        val json = """{"riskLevel":"DANGEROUS","confidence":95,"explanation":"Scam detected."}"""

        val result = parseAnalysisResult(json)

        assertEquals(RiskLevel.DANGEROUS, result.riskLevel)
    }

    // Error handling

    @Test(expected = Exception::class)
    fun `parseAnalysisResult throws on invalid JSON`() {
        parseAnalysisResult("not valid json at all")
    }
}
