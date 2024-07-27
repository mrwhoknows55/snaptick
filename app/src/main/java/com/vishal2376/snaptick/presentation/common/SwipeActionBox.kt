package com.vishal2376.snaptick.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vishal2376.snaptick.ui.theme.Blue500
import com.vishal2376.snaptick.ui.theme.Red
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeActionBox(
	item: T,
	onAction: (T) -> Unit,
	bgColor: Color = Red,
	icon: ImageVector = Icons.Default.Delete,
	iconTint: Color = Blue500,
	animationDuration: Int = 300,
	content: @Composable (T) -> Unit
) {
	var isActionDone by remember { mutableStateOf(false) }
	val state: SwipeToDismissBoxState = rememberSwipeToDismissBoxState(
		confirmValueChange = { dismissValue ->
			if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
				isActionDone = true
				true
			} else {
				false
			}
		}
	)

	LaunchedEffect(isActionDone) {
		if (isActionDone) {
			delay(animationDuration.toLong())
			onAction(item)
		}
	}

	AnimatedVisibility(
		visible = !isActionDone,
		exit = fadeOut(tween(animationDuration))
	) {
		SwipeToDismissBox(
			state = state,
			backgroundContent = {
				ActionBackground(dismissState = state, bgColor, icon, iconTint)
			},
			content = {
				content(item)
			},
			enableDismissFromEndToStart = true,
			enableDismissFromStartToEnd = false
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBackground(
	dismissState: SwipeToDismissBoxState,
	bgColor: Color,
	icon: ImageVector,
	iconTint: Color
) {
	var alphaValue = 0f
	var color = Color.Transparent

	if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
		color = bgColor
		alphaValue = 1.0f
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(color, RoundedCornerShape(8.dp))
			.graphicsLayer {
				alpha = alphaValue
			}
			.padding(16.dp), contentAlignment = Alignment.CenterEnd
	) {
		Icon(imageVector = icon, contentDescription = null, tint = iconTint)
	}
}