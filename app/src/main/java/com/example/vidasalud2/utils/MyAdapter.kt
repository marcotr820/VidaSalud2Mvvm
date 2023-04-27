package com.example.vidasalud2.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.vidasalud2.databinding.MyListviewItemBinding

class MyAdapter(
    private val context: Context, private val listString: List<String>): BaseAdapter()
{

    override fun getCount(): Int {
        return listString.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = MyListviewItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        var convertView = convertView
        convertView = binding.root

        binding.tvItem.text = listString[position]
        return convertView
    }


}