package com.vishal2376.snaptick.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vishal2376.snaptick.R
import com.vishal2376.snaptick.presentation.common.h2TextStyle
import com.vishal2376.snaptick.presentation.common.taskTextStyle
import com.vishal2376.snaptick.presentation.settings.common.TopLanguage
import com.vishal2376.snaptick.ui.theme.Blue
import java.util.Locale

@Composable
fun LanguageOptionComponent(defaultLanguage: String, onSelect: (String) -> Unit) {
	var selectedLanguage by remember { mutableStateOf(TopLanguage.ENGLISH) }

	for (language in TopLanguage.entries) {
		if (language.languageCode == defaultLanguage) {
			selectedLanguage = language
			break
		}
	}

	Column(horizontalAlignment = Alignment.CenterHorizontally) {
		Text(
			text = stringResource(R.string.select_language),
			style = h2TextStyle,
			color = MaterialTheme.colorScheme.onPrimary,
		)
		Spacer(modifier = Modifier.height(8.dp))
		Column(Modifier.verticalScroll(rememberScrollState())) {
			TopLanguage.entries.forEach { language ->
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					RadioButton(
						selected = selectedLanguage == language,
						onClick = {
							selectedLanguage = language
							onSelect(selectedLanguage.languageCode)
						},
						colors = RadioButtonDefaults.colors(selectedColor = Blue)
					)
					Text(
						text = language.emoji,
						style = taskTextStyle,
						color = MaterialTheme.colorScheme.onSecondary
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = language.name,
						style = taskTextStyle,
						color = MaterialTheme.colorScheme.onSecondary
					)
				}
			}
		}
	}
}

fun countryCodeToEmojiFlag(countryCode: String): String {
	return countryCode
		.uppercase(Locale.US)
		.map { char ->
			Character.codePointAt("$char", 0) - 0x41 + 0x1F1E6
		}
		.map { codePoint ->
			Character.toChars(codePoint)
		}
		.joinToString(separator = "") { charArray ->
			String(charArray)
		}
}

@Preview
@Composable
fun LanguageOptionComponentPreview() {
	LanguageOptionComponent("en", {})
}