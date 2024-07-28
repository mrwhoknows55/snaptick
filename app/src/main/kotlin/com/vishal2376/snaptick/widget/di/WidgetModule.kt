package com.vishal2376.snaptick.widget.di

import com.vishal2376.snaptick.domain.interactor.AppWidgetInteractor
import com.vishal2376.snaptick.widget.interactor.AppWidgetInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val widgetModule = module {
	single<AppWidgetInteractor> {
		AppWidgetInteractorImpl(androidContext())
	}
}