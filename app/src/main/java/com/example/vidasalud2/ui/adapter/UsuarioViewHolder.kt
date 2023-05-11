package com.example.vidasalud2.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.data.model.providers.RolProvider
import com.example.vidasalud2.databinding.MyItemUsuarioBinding
import com.example.vidasalud2.utils.Constants

class UsuarioViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = MyItemUsuarioBinding.bind(view)

    fun render(usuario: Usuario, onClickListener:(Usuario) -> Unit){

        binding.tvNombreUsuario.text = usuario.userName
        binding.emailtv.text = usuario.email
        if (usuario.isBlocked){
            binding.estadoUsuariotv.text = Constants.opcionesBloqueo[0]
        } else {
            binding.estadoUsuariotv.text = Constants.opcionesBloqueo[1]
        }

        val roles = RolProvider.roles
        val rolUsuario = roles.find { it.id == usuario.rol!![0] }
        binding.rolUsuariotv.text = rolUsuario?.name ?: "Sin rol"

        itemView.setOnClickListener {
            onClickListener( usuario )
        }
    }
}