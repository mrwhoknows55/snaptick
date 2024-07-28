package com.vishal2376.snaptick.widget.worker

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.vishal2376.snaptick.data.repositories.TaskRepository
import com.vishal2376.snaptick.domain.model.Task
import com.vishal2376.snaptick.widget.SnaptickWidget
import com.vishal2376.snaptick.widget.SnaptickWidgetStateDefinition
import com.vishal2376.snaptick.widget.model.toWidgetTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

private const val LOGGER = "WIDGET_DATA_WORKER"

class WidgetTaskUpdateDataWorker(
	private val context: Context,
	params: WorkerParameters,
	private val taskRepository: TaskRepository,
) : CoroutineWorker(context, params) {

	override suspend fun doWork(): Result {
		// fetch today's tasks
		return withContext(Dispatchers.IO) {
			try {
				val tasks = taskRepository.getTodayTasks().firstOrNull()
				val dayOfWeek = LocalDate.now().dayOfWeek.value - 1
				// get repeatable tasks
				val incompleteTasks = tasks?.filter { task ->
					if (task.isRepeated) task.getRepeatWeekList().contains(dayOfWeek)
					else true
				}?.filter { !it.isCompleted }?.map(Task::toWidgetTask).orEmpty()
				// val inCompletedTasks = updatedTodayTasks.filter { !it.isCompleted }
				//update the data for the widget state
				Log.d(LOGGER, "ALL_TASKS $tasks")
				Log.d(LOGGER, "FEW_TASKS $incompleteTasks")

				SnaptickWidgetStateDefinition.updateData(context, incompleteTasks)
				//update the widget
				SnaptickWidget.updateAll(context)
				// results success
				Result.success(
					workDataOf(
						WorkerConstants.WIDGET_DATA_WORKER_SUCCESS_KEY to
								WorkerConstants.WIDGET_DATA_WORKER_SUCCESS_VALUE
					)
				)
			} catch (e: Exception) {
				e.printStackTrace()
				Result.failure(
					workDataOf(WorkerConstants.WIDGET_DATA_WORKER_ERROR_KEY to e.message)
				)
			}
		}
	}

	companion object {

		private const val WORKER_NAME = "WIDGET_DATA_UPDATE_WORKER"
		private const val PERIODIC_WORKER_NAME = "WIDGET_DATA_UPDATE_PERIODIC_WORKER"

		fun enqueueWorker(
			context: Context,
			policy: ExistingWorkPolicy = ExistingWorkPolicy.REPLACE
		) {
			val updateWorker = OneTimeWorkRequestBuilder<WidgetTaskUpdateDataWorker>()
				.addTag(WorkerConstants.WIDGET_WORKER)
				.build()

			val manager = WorkManager.getInstance(context)
			manager.enqueueUniqueWork(WORKER_NAME, policy, updateWorker)
		}

		fun enqueuePeriodicWorker(
			context: Context,
			policy: ExistingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP
		) {

			val timeDifference = Duration.between(LocalTime.now(), LocalTime.MAX)
				// adding an extra 2 second as LocalTime.MAX is 11:59:59
				// then the 2 seconds will really stat the worker after 12
				.plus(Duration.ofSeconds(2))

			val periodicWorker =
				PeriodicWorkRequestBuilder<WidgetTaskUpdateDataWorker>(Duration.ofDays(1))
					.addTag(WorkerConstants.PERIODIC_WORKER)
					.setInitialDelay(timeDifference)
					.build()

			val manager = WorkManager.getInstance(context)
			manager.enqueueUniquePeriodicWork(PERIODIC_WORKER_NAME, policy, periodicWorker)
		}


		fun cancelPeriodicWorker(context: Context) {
			val manager = WorkManager.getInstance(context)
			// cancels the periodic worker
			manager.cancelUniqueWork(PERIODIC_WORKER_NAME)
		}

		fun cancelWorker(context: Context) {
			val manager = WorkManager.getInstance(context)
			// it will cancel the work if not in complete state
			manager.cancelUniqueWork(WORKER_NAME)
		}
	}
}