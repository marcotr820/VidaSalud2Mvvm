package com.example.vidasalud2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vidasalud2.R

class OptionsAdapter(
    private val dataList: List<String>
): RecyclerView.Adapter<OptionsViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OptionsViewHolder(layoutInflater.inflate(R.layout.my_listview_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        val item = dataList[position]
        holder.render(item)
    }

}