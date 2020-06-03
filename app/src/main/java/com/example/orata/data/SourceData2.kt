package com.example.orata.data

import com.example.orata.R

object SourceData2 {
    private val productName = arrayOf(
        "Karkas Jantan",
        "Karkas Joper (include kepala dan ceker) ",
        "Karkas Broiler",
        "Ayam Jantan Ungkep",
        "Ayam Jabro / Broiler",
        "Ayam Ungkep Joper",
        "Kripik Usus Ayam Kampung",
        "Ati Ampela Ayam Kampung"
    )
    private val productPrice = intArrayOf(
        65000,
        70000,
        45000,
        95000,
        35000,
        64500,
        15000,
        17500
    )
    private val productPhoto = intArrayOf(
        R.drawable.pr_karkas1,
        R.drawable.pr_karkas2,
        R.drawable.pr_karkas3,
        R.drawable.pr_ungkep,
        R.drawable.pr_karkas4,
        R.drawable.pr_ungkep2,
        R.drawable.pr_keripik,
        R.drawable.pr_atiampela
    )

    val listData: ArrayList<ModelProduct>
        get() {
            val list = arrayListOf<ModelProduct>()
            for (position in productName.indices) {
                val product = ModelProduct()
                product.name = productName[position]
                product.price = "" //productPrice[position]
                product.photo = "" //productPhoto[position]
                list.add(product)
            }
            return list
        }
}