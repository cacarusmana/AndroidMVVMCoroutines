package com.coroutinesaac.example.di

import android.app.Application
import android.content.Context
import com.coroutinesaac.example.MainApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val mainApp: MainApp) {


    @Provides
    @Singleton
    fun provideApplication(): Application = mainApp

    @Provides
    @Singleton
    fun provideContext(): Context = mainApp.applicationContext

}