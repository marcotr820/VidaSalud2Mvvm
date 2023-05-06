package com.example.vidasalud2.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.Rol
import java.util.*

class RolAutocompleteAdapter(private val context: Context, private val data: List<Rol>) : BaseAdapter(), Filterable {

    private var filteredData: List<Rol> = data

    override fun getCount(): Int {
        return filteredData.size
    }

    override fun getItem(position: Int): Any {
        return filteredData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = data[position].name
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (constraint.isNullOrBlank()) {
                    results.values = data
                    results.count = data.size
                } else {
                    val filteredList = data.filter { it.name == constraint }
                    results.values = filteredList
                    results.count = filteredList.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as? List<Rol> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}