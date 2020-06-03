package com.example.orata.ui.Sample.model

import com.google.gson.annotations.SerializedName

data class ResultData (
    @field:SerializedName("pesan")
    val pesan: String? = null,

    @field:SerializedName("data")
    val data: List<DataStaff>? = null,

    @field:SerializedName("status")
    val status: Int? = null
)