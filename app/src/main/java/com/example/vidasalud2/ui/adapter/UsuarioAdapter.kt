package com.example.vidasalud2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.Usuario

class UsuarioAdapter(
    private var _usuariosList: List<Usuario>,
    private val _onClickListener:(Usuario) -> Unit   //funcion lambda
) : RecyclerView.Adapter<UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsuarioViewHolder(layoutInflater.inflate(R.layout.my_item_usuario, parent, false))
    }

    override fun getItemCount(): Int {
        return _usuariosList.size
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val item = _usuariosList[position]
        holder.render(item, _onClickListener)
    }

    fun buscarUsuario(usuariosLista: List<Usuario>){
        _usuariosList = usuariosLista
        notifyDataSetChanged()
    }
}