package com.iswariya.scammessagedetector.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iswariya.scammessagedetector.presentation.ui.UiState
import com.iswariya.scammessagedetector.presentation.ui.exampleScamMessages
import com.iswariya.scammessagedetector.ui.components.ResultCard
import com.iswariya.scammessagedetector.ui.theme.DangerBg
import com.iswariya.scammessagedetector.ui.theme.DangerBorder
import com.iswariya.scammessagedetector.ui.theme.DangerRed
import com.iswariya.scammessagedetector.ui.theme.NortonOnYellow
import com.iswariya.scammessagedetector.ui.theme.NortonYellow
import com.iswariya.scammessagedetector.ui.theme.WarnAmber

@Composable
fun HomeScreen(
    uiState: UiState,
    onInputChanged: (String) -> Unit,
    onAnalyze: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        //Header
        Text(
            "Check for Scams",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            "Paste a message, URL, or phone number to check if it's a scam.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Input field
        OutlinedTextField(
            value = uiState.inputText,
            onValueChange = onInputChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste suspicious text or URL here...") },
            minLines = 4,
            maxLines = 8,
            trailingIcon = {
                if (uiState.inputText.isNotEmpty()) {
                    IconButton(onClick = { onInputChanged("") }) {
                        Icon(
                            Icons.Filled.Clear, contentDescription = "Clear",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    IconButton(onClick = {
                        clipboardManager.getText()?.text?.let { onInputChanged(it) }
                    }) {
                        Icon(
                            Icons.Filled.ContentPaste, contentDescription = "Paste",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NortonYellow,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = NortonYellow
            ),
            shape = RoundedCornerShape(12.dp)
        )

        //Analyze button
        Button(
            onClick = { keyboard?.hide(); onAnalyze() },
            enabled = uiState.inputText.isNotBlank() && !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NortonYellow,
                disabledContainerColor = NortonYellow.copy(alpha = 0.35f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = NortonOnYellow
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Analyzing…",
                    style = MaterialTheme.typography.labelLarge,
                    color = NortonOnYellow
                )
            } else {
                Icon(
                    Icons.Filled.Search, contentDescription = null,
                    modifier = Modifier.size(18.dp), tint = NortonOnYellow
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Analyze",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = NortonOnYellow
                )
            }
        }

        // Result
        AnimatedVisibility(
            visible = uiState.result != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            uiState.result?.let { ResultCard(it) }
        }

        // Error
        AnimatedVisibility(
            visible = uiState.error != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            uiState.error?.let { error ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DangerBg,
                    border = BorderStroke(0.5.dp, DangerBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Warning, contentDescription = null,
                            tint = DangerRed, modifier = Modifier.size(18.dp)
                        )
                        Text(
                            error,
                            style = MaterialTheme.typography.bodySmall,
                            color = DangerRed
                        )
                    }
                }
            }
        }

        // Example messages
        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)

        Text(
            "Try an example",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            "Tap any example below to auto-fill the input field.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        exampleScamMessages.forEach { example ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onInputChanged(example) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Filled.Warning, contentDescription = null,
                        tint = WarnAmber,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 1.dp)
                    )
                    Text(
                        example,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
