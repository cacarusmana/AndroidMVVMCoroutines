package com.berkah.alfamet.view.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.berkah.alfamet.R
import com.berkah.alfamet.model.TProduct
import com.berkah.alfamet.util.bottomSheetConfirmationDialog
import com.berkah.alfamet.util.gone
import com.berkah.alfamet.util.onChange
import com.berkah.alfamet.util.visible
import com.berkah.alfamet.view.adapter.MainAdapter
import com.berkah.alfamet.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.math.BigDecimal

class MainActivity : BaseActivity() {

    companion object {
        const val INSERT_CODE = 10
        const val UPDATE_CODE = 11
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponent()
    }

    private fun initComponent() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainAdapter = MainAdapter(this, mutableListOf()) {
            nextActivity(it, UPDATE_CODE)
        }

        rvProducts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mainAdapter
        }

        initObserver()
        initListener()
    }

    private fun initListener() {
        etSearch.onChange { mainViewModel.search(it) }

        fab.setOnClickListener {
            nextActivity(requestCode = INSERT_CODE)
        }
    }

    private fun initObserver() {
        mainViewModel.products.observe(this, Observer { products ->

            products?.let {
                mainAdapter.notifyDataSetChanged(it)

                if (it.isEmpty())
                    tvEmpty.visible()
                else
                    tvEmpty.gone()
            }

        })
    }

    private fun nextActivity(product: TProduct? = null, requestCode: Int) {
        Intent(this, AddProductActivity::class.java).apply {
            if (requestCode == UPDATE_CODE)
                putExtra("object", product)
            else
                putExtra("object", TProduct(productName = "", price = BigDecimal.ZERO))
            startActivityForResult(this, requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            with(data?.getParcelableExtra<TProduct>("object")!!) {
                when (requestCode) {
                    INSERT_CODE -> {
                        mainViewModel.save(this)
                    }
                    UPDATE_CODE -> {
                        if (data.getBooleanExtra("delete", false))
                            mainViewModel.delete(this)
                        else
                            mainViewModel.save(this)
                    }
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_settings -> finish()
            R.id.action_clear_data -> {
                bottomSheetConfirmationDialog(getString(R.string.delete_all_question)) { mainViewModel.deleteAll() }
            }
        }

        return true
    }


}
