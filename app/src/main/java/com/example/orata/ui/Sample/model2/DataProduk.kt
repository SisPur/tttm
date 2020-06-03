package com.example.orata.ui.Sample.model2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DataProduk : Serializable {

    @field:SerializedName("id")
    val staffId: String? = null

    @field:SerializedName("sku")
    val sku: String? = null

    @field:SerializedName("name")
    val name: String? = null

    @field:SerializedName("description")
    val desc: String? = null

    @field:SerializedName("cover")
    val cover: String? = null

    @field:SerializedName("quantity")
    val qty: Int? = null

    @field:SerializedName("price")
    val price: String? = null

    @field:SerializedName("brand_id")
    val brand_id: String? = null

    @field:SerializedName("weight")
    val weight: String? = null

    @field:SerializedName("mass_unit")
    val mass_unit: String? = null

    @field:SerializedName("sale_price")
    val sale_price: String? = null

    @field:SerializedName("length")
    val length: String? = null

    @field:SerializedName("width")
    val width: String? = null

    @field:SerializedName("height")
    val height: String? = null

    @field:SerializedName("slug")
    val slug: String? = null
}