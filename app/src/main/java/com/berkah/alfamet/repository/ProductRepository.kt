package com.berkah.alfamet.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import com.berkah.alfamet.database.AppDatabase
import com.berkah.alfamet.model.TProduct

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