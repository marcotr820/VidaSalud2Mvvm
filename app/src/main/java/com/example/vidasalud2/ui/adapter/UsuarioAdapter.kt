package com.example.vidasalud2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.Usuario

class UsuarioAdapter(
    private val usuariosList: List<Usuario>,
    private val onClickListener:(Usuario) -> Unit   //funcion lambda
) : RecyclerView.Adapter<UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsuarioViewHolder(layoutInflater.inflate(R.layout.item_usuario, parent, false))
    }

    override fun getItemCount(): Int {
        return usuariosList.size
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val item = usuariosList[position]
        holder.render(item, onClickListener)
    }
}