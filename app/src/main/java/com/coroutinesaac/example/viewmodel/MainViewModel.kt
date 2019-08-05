package com.coroutinesaac.example.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.coroutinesaac.example.MainApp
import com.coroutinesaac.example.model.TProduct
import com.coroutinesaac.example.repository.ProductRepository
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel(), CoroutineScope {

    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    @Inject
    lateinit var productRepository: ProductRepository

    val products: MutableLiveData<MutableList<TProduct>> = MutableLiveData()
    val loadingState: MutableLiveData<Int> = MutableLiveData()


    init {
        MainApp.appComponent.inject(this)

        loadingState.value = View.VISIBLE

        launch(Dispatchers.Main) {
//            insertDummy()

            findProducts()
        }
    }

    private suspend fun insertDummy() = withContext(Dispatchers.IO) {
        repeat(1_000) {
            val product = TProduct(productName = "product $it", price = (100 * it).toBigDecimal())
            productRepository.save(product)
            Timber.i("loop at $it")
        }

    }


    fun findProducts(value: String = "") {
        launch(Dispatchers.Main) {
            loadingState.value = View.VISIBLE

            val items = withContext(Dispatchers.IO) {
                with(productRepository) {
                    if (value == "") getAll() else findProducts(value)
                }
            }

            products.value = items
            loadingState.value = View.GONE
        }
    }

    fun save(product: TProduct) {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                productRepository.save(product)
            }

            findProducts()
        }
    }

    fun delete(product: TProduct) {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                productRepository.delete(product)
            }

            findProducts()
        }
    }

    fun deleteAll() {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                productRepository.deleteAll()
            }

            findProducts()
        }
    }

    override fun onCleared() {
        coroutineContext.cancel()
        super.onCleared()
    }
}