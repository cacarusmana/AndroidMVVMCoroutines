package com.berkah.alfamet.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.berkah.alfamet.model.TProduct
import com.berkah.alfamet.repository.ProductRepository
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository = ProductRepository(application)
    val products: MutableLiveData<MutableList<TProduct>> = MutableLiveData()
    private lateinit var allProducts: MutableList<TProduct>

    init {
        getAll()
    }

    private fun getAll() {
        doAsync {
            allProducts = productRepository.getAll()
            uiThread {
                products.value = allProducts
            }
        }
    }

    fun search(value: String) {
        var searchList: MutableList<TProduct> = mutableListOf()
        searchList = allProducts.filter {
            it.productName.toLowerCase().contains(value.toLowerCase()) || it.price.toPlainString().contains(value)
        }.toMutableList()
        products.value = searchList
    }

    fun save(product: TProduct) {
        productRepository.save(product)
        getAll()
    }

    fun delete(product: TProduct) {
        productRepository.delete(product)
        getAll()
    }

    fun deleteAll() {
        productRepository.deleteAll()
        getAll()
    }
}