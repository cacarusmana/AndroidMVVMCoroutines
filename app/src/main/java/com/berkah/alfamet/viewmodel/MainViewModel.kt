package com.berkah.alfamet.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.berkah.alfamet.model.TProduct
import com.berkah.alfamet.repository.ProductRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private val productRepository = ProductRepository(application)
    var products: MutableLiveData<MutableList<TProduct>> = MutableLiveData()
    private lateinit var allProducts: MutableList<TProduct>

    init {
        launch {
            fetchAndShowData()
        }
    }

    private suspend fun getAll() {
        allProducts = withContext(Dispatchers.IO) { productRepository.getAll() }
    }

    private fun fetchAndShowData(isSearch: Boolean = false, searchValue: String = "") {
        launch {

            if (isSearch) {
                val filteredProducts = withContext(Dispatchers.Default) {
                    allProducts.filter {
                        it.productName.toLowerCase().contains(searchValue.toLowerCase())
                                || it.price.toPlainString().contains(searchValue)
                    }.toMutableList()
                }

                showData(filteredProducts)
            } else {
                getAll()
                showData()
            }

        }
    }


    fun search(value: String) {
        fetchAndShowData(true, value)
    }

    private fun showData(products: MutableList<TProduct> = allProducts) {
        this.products.value = products
    }

    fun save(product: TProduct) {
        launch {
            launch(Dispatchers.IO) {
                productRepository.save(product)
            }

            fetchAndShowData()
        }
    }

    fun delete(product: TProduct) {
        launch {
            launch(Dispatchers.IO) {
                productRepository.delete(product)
            }

            fetchAndShowData()
        }
    }

    fun deleteAll() {
        launch {
            launch(Dispatchers.IO) {
                productRepository.deleteAll()
            }

            fetchAndShowData()
        }
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}