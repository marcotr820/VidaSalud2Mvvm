package com.example.vidasalud2.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.databinding.ItemUsuarioBinding
import com.example.vidasalud2.utils.Constants

class UsuarioViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemUsuarioBinding.bind(view)

    fun render(usuario: Usuario, onClickListener:(Usuario) -> Unit){
        binding.tvNombreUsuario.text = usuario.userName
        binding.emailtv.text = usuario.email
        binding.rolUsuariotv.text = usuario.rol!![0]

        if (usuario.isBlocked){
            binding.estadoUsuariotv.text = Constants.opcionesBloqueo[0]
        } else {
            binding.estadoUsuariotv.text = Constants.opcionesBloqueo[1]
        }

        itemView.setOnClickListener {
            onClickListener( usuario )
        }
    }
}