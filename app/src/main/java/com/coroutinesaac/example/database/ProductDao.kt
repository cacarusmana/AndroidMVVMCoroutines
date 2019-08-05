package com.coroutinesaac.example.database

import android.arch.persistence.room.*
import com.coroutinesaac.example.model.TProduct
import java.math.BigDecimal

@Dao
interface ProductDao {

    @Query("select * from TProduct order by productName")
    fun getAll(): MutableList<TProduct>

    @Query("select * from TProduct WHERE productName LIKE :value OR price LIKE :value order by productName")
    fun findProducts(value: String): MutableList<TProduct>

    @Insert
    fun insert(tProduct: TProduct)

    @Update
    fun update(tProduct: TProduct)

    @Delete
    fun delete(tProduct: TProduct)

    @Query("delete from TProduct")
    fun deleteAll()
}