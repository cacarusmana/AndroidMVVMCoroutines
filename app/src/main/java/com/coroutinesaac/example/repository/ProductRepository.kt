package com.coroutinesaac.example.repository

import com.coroutinesaac.example.database.ProductDao
import com.coroutinesaac.example.model.TProduct

class ProductRepository(private val productDao: ProductDao) {


    fun getAll(): MutableList<TProduct> = productDao.getAll()

    fun findProducts(value: String): MutableList<TProduct> = productDao.findProducts("%$value%")

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