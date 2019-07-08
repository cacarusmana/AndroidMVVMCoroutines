package com.coroutinesaac.example.database

import android.arch.persistence.room.*
import com.coroutinesaac.example.model.TProduct
import java.math.BigDecimal

@Dao
interface ProductDao {

    @Query("select * from TProduct order by productName")
    fun getAll(): MutableList<TProduct>

    @Query("select * from TProduct WHERE productName LIKE :productName OR price LIKE :price order by productName")
    fun find(productName: String, price: BigDecimal): MutableList<TProduct>

    @Insert
    fun insert(tProduct: TProduct)

    @Update
    fun update(tProduct: TProduct)

    @Delete
    fun delete(tProduct: TProduct)

    @Query("delete from TProduct")
    fun deleteAll()
}