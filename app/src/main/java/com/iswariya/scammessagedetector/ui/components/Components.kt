package com.iswariya.scammessagedetector.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iswariya.scammessagedetector.domain.model.AnalysisResult
import com.iswariya.scammessagedetector.domain.model.RiskLevel
import com.iswariya.scammessagedetector.ui.theme.DangerBg
import com.iswariya.scammessagedetector.ui.theme.DangerBorder
import com.iswariya.scammessagedetector.ui.theme.DangerRed
import com.iswariya.scammessagedetector.ui.theme.SafeBg
import com.iswariya.scammessagedetector.ui.theme.SafeBorder
import com.iswariya.scammessagedetector.ui.theme.SafeGreen
import com.iswariya.scammessagedetector.ui.theme.WarnAmber
import com.iswariya.scammessagedetector.ui.theme.WarnBg
import com.iswariya.scammessagedetector.ui.theme.WarnBorder

private data class RiskStyle(
    val accent: Color,
    val bg: Color,
    val border: Color,
    val icon: ImageVector
)

@Composable
private fun riskStyle(level: RiskLevel): RiskStyle {
    val isDark = isSystemInDarkTheme()
    return when (level) {
        RiskLevel.SAFE -> RiskStyle(
            if (isDark) Color(0xFF76C442) else SafeGreen,
            if (isDark) Color(0xFF0C2410) else SafeBg,
            if (isDark) Color(0xFF2E7020) else SafeBorder,
            Icons.Filled.CheckCircle
        )

        RiskLevel.SUSPICIOUS -> RiskStyle(
            if (isDark) Color(0xFFFFBF40) else WarnAmber,
            if (isDark) Color(0xFF2A1E00) else WarnBg,
            if (isDark) Color(0xFF7A5500) else WarnBorder,
            Icons.Filled.Warning
        )

        RiskLevel.DANGEROUS -> RiskStyle(
            if (isDark) Color(0xFFEF5350) else DangerRed,
            if (isDark) Color(0xFF2D1010) else DangerBg,
            if (isDark) Color(0xFF8B2020) else DangerBorder,
            Icons.Filled.Cancel
        )
    }
}

@Composable
fun ResultCard(result: AnalysisResult) {
    val style = riskStyle(result.riskLevel)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = style.bg,
        border = BorderStroke(0.5.dp, style.border),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        style.icon, contentDescription = null,
                        tint = style.accent, modifier = Modifier.size(22.dp)
                    )
                    Text(
                        result.riskLevel.label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = style.accent
                    )
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = style.accent.copy(alpha = 0.15f)
                ) {
                    Text(
                        "${result.confidence}% confident",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = style.accent,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(style.accent.copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(result.confidence / 100f)
                        .fillMaxHeight()
                        .background(style.accent)
                )
            }

            Text(
                result.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
