package com.example.orata.utils

import android.util.Log
import com.luseen.datelibrary.DateHelper
import java.text.NumberFormat
import java.util.*

object MyUtilities {
    const val KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID"
    private val TAG = MyUtilities::class.java.simpleName
    const val url = "jdbc:mysql://xxx.xxx.xxx.xxx:3306/xxx"
    const val user = "xxx"
    const val pass = "xxx"
    fun generateNumber(min: Int, max: Int): Int {
        require(min < max) { "max must greater than min" }
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }

    fun formatPrice(raw_price: String): String {
        val data: Array<String?> = raw_price.split("\\.").toTypedArray()
        if (data[0] != null) {
            val f =
                NumberFormat.getCurrencyInstance(Locale.US)
            var new_format = f.format(Integer.valueOf(data[0]!!))
            new_format = new_format.replace("$", "")
            val data_0 = new_format.split("\\.").toTypedArray()
            if (data_0[0] != null) {
                new_format = data_0[0]
                new_format = new_format.replace(",", ".")
                return new_format
            }
        }
        return raw_price
    }

    fun fromDateToDBFormat(date: Date?): String {
        var dateString = ""
        try {
            val dateHelper = DateHelper(date)
            dateString = dateHelper.getYear().toString() + "-" +
                    dateHelper.getMonth() + "-" +
                    dateHelper.getDay() + " " +
                    dateHelper.getHour() + ":" +
                    dateHelper.getSeconds()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.message)
        }
        return dateString
    }

    val currentDateTime: Date
        get() {
            val dateHelper = DateHelper(Date())
            return dateHelper.getGivenDate()
        }
}