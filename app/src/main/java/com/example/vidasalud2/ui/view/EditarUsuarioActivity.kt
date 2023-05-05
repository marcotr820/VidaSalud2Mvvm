package com.example.vidasalud2.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.databinding.ActivityEditarUsuarioBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson

class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<MaterialToolbar>(R.id.editarUsuarioToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = "Editar usuario"
            it.setDisplayHomeAsUpEnabled(true)
        }

        val usuarioJson = intent.extras?.getString("usuario")
        val usuario = Gson().fromJson(usuarioJson, Usuario::class.java)

        binding.usuariotv.text = usuario.toString()
        binding.correotv.text = usuario.email
    }

    //evento volver atras toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}