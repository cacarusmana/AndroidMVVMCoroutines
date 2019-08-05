package com.coroutinesaac.example.di

import com.coroutinesaac.example.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, RepositoryModule::class, DatabaseModule::class])
@Singleton
interface AppComponent {

    fun inject(injector: MainViewModel)

}