package com.courierservice.challenge.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.courierservice.challenge.data.models.PackageDetails
import com.courierservice.challenge.databinding.CourierAdapterLayoutBinding

class CourierServiceAdapter(val context: Context, private val itemList: MutableList<PackageDetails>) :
    RecyclerView.Adapter<CourierServiceAdapter.CourierServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourierServiceViewHolder {
        val courierAdapterLayoutBinding = CourierAdapterLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return CourierServiceViewHolder(courierAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: CourierServiceViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.pkgIdTextView.text = currentItem.pkgId
        holder.discountTextView.text = currentItem.discount
        holder.totalCostTextView.text = currentItem.totalCost
        if (!currentItem.deliveryTime.equals("")){
            holder.deliveryTimeTextView.visibility = View.VISIBLE
            holder.deliveryTimeTextView.text = currentItem.deliveryTime
        }else{
            holder.deliveryTimeTextView.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class CourierServiceViewHolder(courierAdapterLayoutBinding: CourierAdapterLayoutBinding) : RecyclerView.ViewHolder(courierAdapterLayoutBinding.root) {
        val pkgIdTextView: TextView = courierAdapterLayoutBinding.pkgId
        val discountTextView: TextView = courierAdapterLayoutBinding.discount
        val totalCostTextView: TextView = courierAdapterLayoutBinding.totalCost
        val deliveryTimeTextView: TextView = courierAdapterLayoutBinding.deliveryTime
    }
}