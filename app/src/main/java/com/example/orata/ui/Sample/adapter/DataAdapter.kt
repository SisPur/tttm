package com.example.crud1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.example.crud1.R
import com.example.orata.R
import com.example.orata.ui.Sample.model.DataStaff
import kotlinx.android.synthetic.main.item_data.view.*
//import org.jetbrains.anko.sdk27.coroutines.onClick

//class AdapterMain (val listProduct : ArrayList<ModelProduct>) : RecyclerView.Adapter<AdapterMain.ViewHolder>() {
class DataAdapter(val data: List<DataStaff>?, private val click: OnClickItem) : RecyclerView.Adapter<DataAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data,parent,false)
        /*val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_data,parent,false)*/
        return MyHolder(view)
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.onBind(data?.get(position))
        holder.itemView.setOnClickListener { view ->
            click.clicked(data?.get(position))
        }
        holder.itemView.btnHapus.setOnClickListener {
            click.delete(data?.get(position))
        }
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(get: DataStaff?) {
            itemView.tvName.text = get?.staffName
            itemView.tvPhone.text = get?.staffHp
            itemView.tvAddress.text = get?.staffAlamat
        }
    }

    interface OnClickItem{
        fun clicked (staff: DataStaff?)
        fun delete(staff: DataStaff?)

    }

}