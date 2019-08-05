package com.coroutinesaac.example

import android.app.Application
import com.coroutinesaac.example.di.*
import timber.log.Timber

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .repositoryModule(RepositoryModule())
            .databaseModule(DatabaseModule())
            .build()

    }

    companion object {
        lateinit var appComponent: AppComponent
    }

}