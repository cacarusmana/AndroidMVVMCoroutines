package com.coroutinesaac.example.di

import com.coroutinesaac.example.database.ProductDao
import com.coroutinesaac.example.repository.ProductRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(productDao: ProductDao): ProductRepository {
        return ProductRepository(productDao)
    }
}