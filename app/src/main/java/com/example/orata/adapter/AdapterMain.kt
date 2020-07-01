package com.example.orata.ui.Home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.orata.R
import com.example.orata.data.ModelProduct
import com.example.orata.DetailActivity
import com.squareup.picasso.Picasso
import java.text.DecimalFormat


class AdapterMain (val listProduct : ArrayList<ModelProduct>) : RecyclerView.Adapter<AdapterMain.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val (nama, price, photo, note, weight) = listProduct[position]

//        Glide.with(holder.itemView.context)
//            .load(photo)
//            .into(holder.ivPhoto)

        holder.tvName.text = nama

        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.setGroupingUsed(true)
        decimalFormat.setGroupingSize(3)

        val x: List<String> = price.split(".")
        val number : String = x[0]

        holder.tvPrice.text = "Rp "+decimalFormat.format((Integer.valueOf(number)))

        Picasso.with(holder.itemView.context).load("http://tttm.tiketantrian.com/public/storage/" + photo).placeholder(R.mipmap.ic_launcher).into(holder.ivPhoto)
        Log.d("tes", "Load image " + photo)

        holder.cvProduct.setOnClickListener {
            var intent : Intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("name", nama)
            intent.putExtra("price", "Rp "+decimalFormat.format((Integer.valueOf(number))))
            intent.putExtra("photo", photo)
            intent.putExtra("weight", weight)
            intent.putExtra("expired", "dd/MM/yyyy")
            holder.itemView.context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName : TextView = itemView.findViewById(R.id.product_name)
        var tvPrice : TextView = itemView.findViewById(R.id.product_price)
        var ivPhoto : ImageView = itemView.findViewById(R.id.product_photo)
        var cvProduct : CardView = itemView.findViewById(R.id.cvProduct)
    }
}