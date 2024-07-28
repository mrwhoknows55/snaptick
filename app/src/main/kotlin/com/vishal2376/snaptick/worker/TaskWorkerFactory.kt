package com.vishal2376.snaptick.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.vishal2376.snaptick.data.repositories.TaskRepository
import com.vishal2376.snaptick.widget.worker.WidgetTaskUpdateDataWorker

class TaskWorkerFactory(private val taskRepository: TaskRepository) : WorkerFactory() {

	override fun createWorker(
		appContext: Context, workerClassName: String, workerParameters: WorkerParameters
	): ListenableWorker? {
		return when (workerClassName) {
			RepeatTaskWorker::class.java.name -> RepeatTaskWorker(
				appContext, workerParameters, taskRepository
			)

			WidgetTaskUpdateDataWorker::class.java.name -> WidgetTaskUpdateDataWorker(
				appContext, workerParameters, taskRepository
			)

			NotificationWorker::class.java.name -> NotificationWorker(
				appContext, workerParameters
			)

			else -> null
		}
	}
}