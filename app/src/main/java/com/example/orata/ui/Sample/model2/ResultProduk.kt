package com.example.orata.ui.Sample.model2

import com.example.orata.ui.Sample.model.DataStaff
import com.google.gson.annotations.SerializedName

data class ResultProduk(
    @field:SerializedName("pesan")
    val pesan: String? = null,

    @field:SerializedName("data")
    val data: List<DataProduk>? = null,

    @field:SerializedName("status")
    val status: Int? = null
)