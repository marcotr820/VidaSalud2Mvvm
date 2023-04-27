package com.example.vidasalud2.ui.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.vidasalud2.databinding.MyListviewItemBinding

class OptionsViewHolder(view: View): ViewHolder(view) {

    val binding = MyListviewItemBinding.bind(view)

    fun render(item: String){
        binding.tvItem.text = item

        itemView.setOnClickListener{
            Toast.makeText(binding.tvItem.context, "administrar", Toast.LENGTH_SHORT).show()
        }
    }
}