package com.vishal2376.snaptick.di

import androidx.room.Room
import androidx.work.WorkerFactory
import com.vishal2376.snaptick.data.local.MIGRATION_1_2
import com.vishal2376.snaptick.data.local.TaskDao
import com.vishal2376.snaptick.data.local.TaskDatabase
import com.vishal2376.snaptick.data.repositories.TaskRepository
import com.vishal2376.snaptick.presentation.viewmodels.TaskViewModel
import com.vishal2376.snaptick.widget.worker.WidgetTaskUpdateDataWorker
import com.vishal2376.snaptick.worker.NotificationWorker
import com.vishal2376.snaptick.worker.RepeatTaskWorker
import com.vishal2376.snaptick.worker.TaskWorkerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
	single<TaskDatabase> {
		Room.databaseBuilder(context = androidContext(), TaskDatabase::class.java, "local_db")
			.fallbackToDestructiveMigration().addMigrations(MIGRATION_1_2).build()
	}

	single<TaskDao> {
		get<TaskDatabase>().taskDao()
	}

	single<TaskRepository> {
		TaskRepository(dao = get(), interactor = get())
	}

	viewModel<TaskViewModel> { TaskViewModel(get()) }

	factory<WorkerFactory> { TaskWorkerFactory(get()) }

	worker<RepeatTaskWorker> {
		RepeatTaskWorker(
			context = androidContext(), params = get(), repository = get()
		)
	}
	worker<WidgetTaskUpdateDataWorker> {
		WidgetTaskUpdateDataWorker(
			context = androidContext(), params = get(), taskRepository = get()
		)
	}

	worker<NotificationWorker> {
		NotificationWorker(
			context = androidContext(), params = get()
		)
	}
}