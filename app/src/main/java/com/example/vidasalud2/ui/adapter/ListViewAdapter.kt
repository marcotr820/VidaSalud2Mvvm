package com.example.vidasalud2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.vidasalud2.databinding.MyListviewItemBinding

class ListViewAdapter(
    private val dataList: List<String>
): BaseAdapter()
{

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val binding: MyListviewItemBinding
        var view = convertView

        if (view == null) {
            binding = MyListviewItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            view = binding.root
        } else {
            binding = MyListviewItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        }
        //val binding = MyListviewItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        //var convertView = binding.root

        binding.tvItem.text = dataList[position]
        return view
    }


}