package com.example.vidasalud2.ui.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.databinding.ItemUsuarioBinding

class UsuarioViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemUsuarioBinding.bind(view)

    fun render(usuario: Usuario, onClickListener:(Usuario) -> Unit){
        binding.tvNombreUsuario.text = usuario.userName

        itemView.setOnClickListener {
            onClickListener( usuario )
            //Toast.makeText(binding.usuarioContainer.context, usuario.email, Toast.LENGTH_SHORT).show()
        }
    }
}