package com.example.vidasalud2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.Usuario

class UsuarioAdapter(
    private var _usuariosOriginalList: List<Usuario>,
    private val _onClickListener:(Usuario) -> Unit   //funcion lambda
) : RecyclerView.Adapter<UsuarioViewHolder>() {

    private var usuariosFiltrados: List<Usuario> = _usuariosOriginalList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsuarioViewHolder(layoutInflater.inflate(R.layout.my_item_usuario, parent, false))
    }

    override fun getItemCount(): Int {
        return usuariosFiltrados.size
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val item = usuariosFiltrados[position]
        holder.render(item, _onClickListener)
    }

    fun buscarUsuario(query: String){
        usuariosFiltrados = if (query.isEmpty()){
            _usuariosOriginalList
        } else {
            _usuariosOriginalList.filter { it.userName.contains(query, true) }
        }

        notifyDataSetChanged()
    }
}