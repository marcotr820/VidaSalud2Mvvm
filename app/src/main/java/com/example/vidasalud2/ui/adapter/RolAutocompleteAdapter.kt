package com.example.vidasalud2.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.Rol
import java.util.*

class RolAutocompleteAdapter(private val context: Context, private val rolList: List<Rol>) : BaseAdapter(),
    Filterable {
    private var filteredUserList: List<Rol> = rolList

    override fun getCount(): Int = filteredUserList.size

    override fun getItem(position: Int): Rol = filteredUserList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: TextView = convertView as? TextView ?: LayoutInflater.from(context).inflate(R.layout.my_item_dropdown, parent, false) as TextView
        val user = filteredUserList[position]
        view.text = user.name
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val query = constraint?.toString()?.toLowerCase(Locale.ROOT) ?: ""
                filteredUserList = if (query.isEmpty()) {
                    rolList
                } else {
                    rolList.filter { it.name.toLowerCase(Locale.ROOT).contains(query) }
                }
                results.values = filteredUserList
                results.count = filteredUserList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredUserList = results?.values as? List<Rol> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}