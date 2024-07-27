package com.vishal2376.snaptick.presentation.settings.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vishal2376.snaptick.R
import com.vishal2376.snaptick.presentation.common.NativeTimePickerDialog
import com.vishal2376.snaptick.presentation.common.ShowTimePicker
import com.vishal2376.snaptick.presentation.common.h2TextStyle
import com.vishal2376.snaptick.presentation.common.h3TextStyle
import com.vishal2376.snaptick.presentation.common.taskTextStyle
import com.vishal2376.snaptick.ui.theme.Black500
import com.vishal2376.snaptick.ui.theme.Blue
import com.vishal2376.snaptick.ui.theme.SnaptickTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimePickerOptionComponent(isWheelTimePicker: Boolean, onSelect: (Boolean) -> Unit) {
	var selectedOption by remember { mutableStateOf(isWheelTimePicker) }
	var showDialogTimePicker by remember { mutableStateOf(false) }
	val selectedTime by remember { mutableStateOf(LocalTime.now()) }

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {

		if (showDialogTimePicker) {
			NativeTimePickerDialog(selectedTime, onClose = { showDialogTimePicker = false })
		}

		Text(
			text = stringResource(R.string.choose_time_picker_style),
			style = h2TextStyle,
			color = MaterialTheme.colorScheme.onPrimary,
		)
		Spacer(modifier = Modifier.height(8.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceAround
		) {
			ShowTimePicker(time = LocalTime.now(), onSelect = {})
			Row(
				modifier = Modifier
					.clip(RoundedCornerShape(16.dp))
					.clickable { showDialogTimePicker = true }
					.border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
					.padding(10.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Icon(
					imageVector = Icons.Default.AccessTime,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.onPrimary,
					modifier = Modifier.size(24.dp)
				)
				val dtf = DateTimeFormatter.ofPattern("hh : mm a")
				Text(
					text = selectedTime.format(dtf),
					style = taskTextStyle,
					color = MaterialTheme.colorScheme.onPrimary
				)
			}
		}

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp),
			horizontalArrangement = Arrangement.SpaceAround
		) {
			ToggleOptions(title = stringResource(R.string.scroll), isSelected = selectedOption) {
				selectedOption = true
				onSelect(true)
			}
			ToggleOptions(title = stringResource(R.string.clock), isSelected = !selectedOption) {
				selectedOption = false
				onSelect(false)
			}
		}
	}

}

@Composable
fun ToggleOptions(title: String, isSelected: Boolean, onClick: () -> Unit) {
	Column(
		verticalArrangement = Arrangement.spacedBy(4.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Box(
			modifier = Modifier
				.clip(RoundedCornerShape(8.dp))
				.clickable { onClick() }
				.background(if (isSelected) Blue else MaterialTheme.colorScheme.primary),
			contentAlignment = Alignment.Center
		) {
			Text(
				text = title,
				style = h3TextStyle,
				color = if (isSelected) Black500 else MaterialTheme.colorScheme.onPrimary,
				modifier = Modifier.padding(24.dp, 8.dp)
			)
		}
		if (isSelected) {

			val animValue = remember { Animatable(initialValue = 0f) }

			LaunchedEffect(Unit) {
				animValue.animateTo(1f, tween(300))
			}

			Box(
				modifier = Modifier
					.width(40.dp * animValue.value)
					.height(4.dp)
					.background(Blue, RoundedCornerShape(8.dp))
			)
		}
	}
}

@Preview
@Composable
fun TimePickerOptionComponentPreview() {
	SnaptickTheme {
		TimePickerOptionComponent(false, onSelect = {})
	}
}