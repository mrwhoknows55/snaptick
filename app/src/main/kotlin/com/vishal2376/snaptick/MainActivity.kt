package com.vishal2376.snaptick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.vishal2376.snaptick.presentation.common.CustomSnackBar
import com.vishal2376.snaptick.presentation.navigation.AppNavigation
import com.vishal2376.snaptick.presentation.viewmodels.TaskViewModel
import com.vishal2376.snaptick.ui.theme.SnaptickTheme
import com.vishal2376.snaptick.util.NotificationHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

	private val taskViewModel: TaskViewModel by viewModel()
	private lateinit var notificationHelper: NotificationHelper

	override fun onCreate(savedInstanceState: Bundle?) {
		// init splash screen
		installSplashScreen()

		super.onCreate(savedInstanceState)

		// create notification channel
		notificationHelper = NotificationHelper(applicationContext)
		notificationHelper.createNotificationChannel()

		// load app state
		taskViewModel.loadAppState(applicationContext)

		setContent {
			SnaptickTheme(theme = taskViewModel.appState.theme) {
				AppNavigation(taskViewModel = taskViewModel)
				CustomSnackBar()
			}
		}
	}
}