package com.berkah.alfamet.model

import android.arch.persistence.room.TypeConverter
import java.math.BigDecimal

class Converters {

    @TypeConverter
    fun fromBigDecimalToString(value: BigDecimal): String {
        return value.toPlainString()
    }

    @TypeConverter
    fun fromStringToBigDecimal(value: String): BigDecimal {
        return BigDecimal(value)
    }
}