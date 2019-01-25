package com.berkah.alfamet.view.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.berkah.alfamet.R
import com.berkah.alfamet.model.TProduct
import kotlinx.android.synthetic.main.activity_add_product.*
import java.math.BigDecimal

class AddProductActivity : AppCompatActivity() {

    private lateinit var product: TProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        initComponent()
    }

    private fun initComponent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        product = intent.getParcelableExtra<TProduct>("object")

        if (product.id == 0)
            btn_delete.visibility = View.GONE

        et_product_name.setText(product.productName)
        et_price.setText(if (product.price == BigDecimal.ZERO) "" else product.price.toPlainString())

        btn_save.setOnClickListener { view ->
            var msg = ""

            if (TextUtils.isEmpty(et_product_name.text))
                msg = getString(R.string.product_name_blank)
            else if (TextUtils.isEmpty(et_price.text))
                msg = getString(R.string.price_blank)
            else if (et_price.text.toString().toInt() <= 0)
                msg = getString(R.string.price_zero)

            if (!msg.equals("")) {
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
            } else {
                product.apply {
                    productName = et_product_name.text.toString()
                    price = BigDecimal(et_price.text.toString())
                }

                val intent = Intent()
                intent.putExtra("object", product)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        btn_delete.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.delete_question))
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    val intent = Intent()
                    intent.putExtra("object", product)
                    intent.putExtra("delete", true)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }.setNegativeButton(getString(R.string.no), null).show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
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