package com.coroutinesaac.example.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Entity()
@Parcelize
data class TProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var productName: String,
    var price: BigDecimal
) : Parcelable