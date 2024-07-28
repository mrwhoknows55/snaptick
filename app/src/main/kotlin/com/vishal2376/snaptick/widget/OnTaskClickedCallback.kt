package com.vishal2376.snaptick.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.vishal2376.snaptick.data.repositories.TaskRepository
import com.vishal2376.snaptick.domain.interactor.AppWidgetInteractor
import org.koin.java.KoinJavaComponent.inject

class OnTaskClickedCallback : ActionCallback {

	private val taskRepository: TaskRepository by inject(TaskRepository::class.java)
	private val glanceInterceptor: AppWidgetInteractor by inject(AppWidgetInteractor::class.java)

	override suspend fun onAction(
		context: Context,
		glanceId: GlanceId,
		parameters: ActionParameters
	) {
		val taskId: Int = parameters[parameterTaskId] ?: -1

		val task = taskRepository.getTaskById(taskId)
		val updatedTask = task.copy(isCompleted = !task.isCompleted)
		// update the task
		taskRepository.updateTask(updatedTask)

		glanceInterceptor.enqueueWidgetDataWorker()
	}
}
