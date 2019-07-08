package com.coroutinesaac.example.repository

import android.app.Application
import com.coroutinesaac.example.database.AppDatabase
import com.coroutinesaac.example.model.TProduct

class ProductRepository(application: Application) {

    private val productDao = AppDatabase.getInstance(application).productDao()

    fun getAll(): MutableList<TProduct> = productDao.getAll()

    fun save(product: TProduct) {
        if (product.id == 0)
            productDao.insert(product)
        else
            productDao.update(product)
    }

    fun delete(product: TProduct) {
        productDao.delete(product)
    }

    fun deleteAll() {
        productDao.deleteAll()
    }
}