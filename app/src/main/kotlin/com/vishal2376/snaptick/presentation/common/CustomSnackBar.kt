package com.vishal2376.snaptick.presentation.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishal2376.snaptick.ui.theme.DarkGreen
import com.vishal2376.snaptick.ui.theme.Red
import com.vishal2376.snaptick.ui.theme.SnaptickTheme
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

object SnackbarController {

	// Triggering event
	val _msg = mutableStateOf<String?>("")
	val msg: State<String?> = _msg

	var delay: Long = 1000
	var actionText: String? = null
	var actionColor: Color = Red
	var onClickAction: () -> Unit = {}


	fun showCustomSnackbar(
		msg: String?,
		delay: Long = 3000,
		actionText: String? = null,
		actionColor: Color = Red,
		onClickAction: () -> Unit = {}
	) {
		SnackbarController.delay = delay
		_msg.value = msg
		SnackbarController.actionText = actionText
		SnackbarController.actionColor = actionColor
		SnackbarController.onClickAction = onClickAction
	}

}

@Composable
fun CustomSnackBar() {

	val snackBarMessage = SnackbarController.msg.value
	val delay = SnackbarController.delay
	val actionText = SnackbarController.actionText
	val actionColor = SnackbarController.actionColor
	val onClickAction: () -> Unit = SnackbarController.onClickAction

	var isDismiss by remember { mutableStateOf(false) }
	var offsetX by remember { mutableFloatStateOf(0f) }
	val offsetXState by animateFloatAsState(targetValue = offsetX, label = "")
	val configuration = LocalConfiguration.current
	val deviceWidthPixels = configuration.screenWidthDp.absoluteValue * LocalDensity.current.density

	// animation
	val translateY = 250f
	val verticalTranslate = remember { Animatable(translateY) }
	val scope = rememberCoroutineScope()

	if (isDismiss) {
		SnackbarController._msg.value = null
		offsetX = 0f
		isDismiss = false
	}

	if (!snackBarMessage.isNullOrBlank()) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(24.dp)
				.graphicsLayer {
					translationY = verticalTranslate.value
				}
				.background(Color.Transparent),
			contentAlignment = BottomCenter,
		) {
			LaunchedEffect(Unit) {
				verticalTranslate.animateTo(
					0f,
					tween(500)
				)
				delay(delay)
				verticalTranslate.animateTo(
					translateY,
					tween(500)
				)
				verticalTranslate.snapTo(translateY)
				isDismiss = true
			}
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.offset { IntOffset(offsetXState.roundToInt(), 0) }
					.draggable(
						orientation = Orientation.Horizontal,
						state = rememberDraggableState { delta ->
							offsetX += delta
						},
						onDragStopped = { endPosition ->
							val threshold = deviceWidthPixels * 0.3f // Dismiss threshold (30%)
							if (endPosition > threshold) {
								offsetX = 11000f
								isDismiss = true
							} else if (endPosition < -threshold) {
								offsetX = 11000f
								isDismiss = true
							} else {
								offsetX = 0f
							}
						}
					)
					.background(actionColor, shape = RoundedCornerShape(8.dp))
					.padding(bottom = 4.dp),
				contentAlignment = Center,
			) {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.background(
							MaterialTheme.colorScheme.secondary,
							RoundedCornerShape(
								topStart = 8.dp,
								topEnd = 8.dp
							)
						)
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
					) {
						Text(
							text = snackBarMessage,
							modifier = Modifier
								.weight(1f)
								.padding(16.dp),
							style = infoDescTextStyle,
							color = MaterialTheme.colorScheme.onSecondary,
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold
						)
						if (!actionText.isNullOrBlank())
							Text(
								text = actionText,
								fontWeight = FontWeight.Bold,
								style = h3TextStyle,
								color = actionColor,
								fontSize = 16.sp,
								modifier = Modifier
									.padding(16.dp)
									.clickable {
										onClickAction.invoke()
									}
							)
					}
				}
			}
		}
	}
}


@Preview(showSystemUi = true)
@Composable
fun SnackBarPreview() {
	SnackbarController.showCustomSnackbar("Hello", actionText = "Ok", actionColor = DarkGreen)
	SnaptickTheme {
		CustomSnackBar()
	}
}