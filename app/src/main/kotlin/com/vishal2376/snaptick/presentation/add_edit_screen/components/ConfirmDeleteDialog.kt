package com.vishal2376.snaptick.presentation.add_edit_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vishal2376.snaptick.R
import com.vishal2376.snaptick.presentation.common.h2TextStyle
import com.vishal2376.snaptick.ui.theme.LightGray
import com.vishal2376.snaptick.ui.theme.Red
import com.vishal2376.snaptick.ui.theme.SnaptickTheme

@Composable
fun ConfirmDeleteDialog(onClose: () -> Unit, onDelete: () -> Unit) {
	Dialog(onDismissRequest = { onClose() }) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.border(4.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(16.dp)),
			shape = RoundedCornerShape(16.dp),
			colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						32.dp
					),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Text(
					text = stringResource(R.string.delete_task),
					color = MaterialTheme.colorScheme.onPrimary,
					style = h2TextStyle
				)
				Image(
					painter = painterResource(id = R.drawable.delete_task),
					contentDescription = null,
					Modifier.size(150.dp)
				)

				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Button(
						onClick = { onClose() },
						colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
						shape = RoundedCornerShape(8.dp),
						border = BorderStroke(
							2.dp,
							LightGray
						)
					) {
						Text(
							text = stringResource(R.string.cancel),
							color = LightGray
						)
					}
					Button(
						onClick = { onDelete() },
						colors = ButtonDefaults.buttonColors(containerColor = Red),
						shape = RoundedCornerShape(8.dp),
					) {
						Text(
							text = stringResource(R.string.delete),
							color = Color.Black
						)
					}
				}
			}
		}
	}
}

@Preview()
@Composable
fun ConfirmDeleteDialogPreview() {
	SnaptickTheme {
		ConfirmDeleteDialog(
			{},
			{})
	}
}