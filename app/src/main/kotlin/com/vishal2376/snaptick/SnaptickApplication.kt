package com.vishal2376.snaptick

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.vishal2376.snaptick.di.appModule
import com.vishal2376.snaptick.util.Constants
import com.vishal2376.snaptick.widget.di.widgetModule
import com.vishal2376.snaptick.worker.RepeatTaskWorker
import org.acra.ACRA
import org.acra.BuildConfig
import org.acra.config.CoreConfigurationBuilder
import org.acra.config.DialogConfigurationBuilder
import org.acra.config.MailSenderConfigurationBuilder
import org.acra.data.StringFormat
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class SnaptickApplication : Application(), KoinComponent, Configuration.Provider {

	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
		ACRA.init(
			this,
			CoreConfigurationBuilder().withBuildConfigClass(BuildConfig::class.java)
				.withReportFormat(StringFormat.JSON).withPluginConfigurations(

					// Dialog configuration:
					DialogConfigurationBuilder().withText(getString(R.string.dialog_text))
						.withTitle(getString(R.string.dialog_title))
						.withPositiveButtonText(getString(R.string.dialog_positive))
						.withNegativeButtonText(getString(R.string.dialog_negative)).build(),

					// Mail sender configuration:
					MailSenderConfigurationBuilder().withMailTo(Constants.EMAIL)
						.withReportFileName("crash_report.txt").withReportAsFile(true).build()
				)
		)
	}

	private val delegatingWorkerFactory = DelegatingWorkerFactory()
	override fun onCreate() {
		super.onCreate()


		startKoin {
			androidLogger()
			androidContext(this@SnaptickApplication)
			workManagerFactory()
			modules(appModule, widgetModule)
		}
		getKoin().getAll<WorkerFactory>().forEach {
			delegatingWorkerFactory.addFactory(it)
		}
		initWorker()
	}

	private fun initWorker() {
		val maxTimeSec = LocalTime.MAX.toSecondOfDay() + 1
		val currentTimeSec = LocalTime.now().toSecondOfDay()

		val delay = (maxTimeSec - currentTimeSec)
		if (delay > 0) {
			startRepeatWorker(delay)
		}
	}

	private fun startRepeatWorker(delay: Int) {
		// repeat task request
		val workRequest =
			PeriodicWorkRequest.Builder(RepeatTaskWorker::class.java, 1, TimeUnit.DAYS)
				.setInitialDelay(delay.toLong(), TimeUnit.SECONDS)
				.build()

		WorkManager.getInstance(applicationContext)
			.enqueueUniquePeriodicWork(
				"Repeat-Tasks",
				ExistingPeriodicWorkPolicy.KEEP,
				workRequest
			)
	}

	override val workManagerConfiguration: Configuration
		get() = Configuration.Builder()
			.setWorkerFactory(delegatingWorkerFactory)
			.apply {
				println("WORKER FACTORIES: workManagerConfiguration called")
				if (BuildConfig.DEBUG) {
					setMinimumLoggingLevel(Log.INFO)
				}
			}
			.build()
}