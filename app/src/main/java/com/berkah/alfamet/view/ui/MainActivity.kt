package com.berkah.alfamet.view.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.berkah.alfamet.R
import com.berkah.alfamet.model.TProduct
import com.berkah.alfamet.view.adapter.MainAdapter
import com.berkah.alfamet.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.doAsync
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    companion object {
        const val INSERT_CODE = 10
        const val UPDATE_CODE = 11
    }

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponent()
    }

    private fun initComponent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mainAdapter = MainAdapter(this, mutableListOf()) {
            onItemClick(it)
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mainAdapter
        }

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.products.observe(this, Observer {
            mainAdapter.notifyChanges(it!!)
            tv_no_data.apply {
                if (it.isEmpty())
                    visibility = View.VISIBLE
                else
                    visibility = View.GONE
            }
        })

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mainViewModel.search(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        fab.setOnClickListener {
            nextActivity(requestCode = INSERT_CODE)
        }
    }

    private fun nextActivity(product: TProduct? = null, requestCode: Int) {
        val intent = Intent(this, AddProductActivity::class.java)
        if (requestCode == UPDATE_CODE)
            intent.putExtra("object", product)
        else
            intent.putExtra("object", TProduct(productName = "", price = BigDecimal.ZERO))
        startActivityForResult(intent, requestCode)
    }

    private fun onItemClick(product: TProduct) {
        nextActivity(product, UPDATE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val product = data?.getParcelableExtra<TProduct>("object")!!

            if (requestCode == INSERT_CODE) {
                doAsync {
                    mainViewModel.save(product)
                }
            } else if (requestCode == UPDATE_CODE) {
                doAsync {
                    if (data.getBooleanExtra("delete", false))
                        mainViewModel.delete(product)
                    else
                        mainViewModel.save(product)
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
                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.delete_all_question))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        doAsync {
                            mainViewModel.deleteAll()
                        }
                    }.setNegativeButton(getString(R.string.no), null).show()
            }
        }

        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
