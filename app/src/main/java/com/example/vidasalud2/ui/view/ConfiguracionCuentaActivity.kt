package com.example.vidasalud2.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vidasalud2.R
import com.example.vidasalud2.databinding.ActivityConfiguracionCuentaBinding
import com.google.android.material.appbar.MaterialToolbar

class ConfiguracionCuentaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<MaterialToolbar>(R.id.configuracionCuentaToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = "Configuración Cuenta"
            it.setDisplayHomeAsUpEnabled(true)
        }

        binding.actualizarUserName.myButtonOption.text = "Nombre de usuario"
        binding.actualizarUserName.myButtonOption.setOnClickListener {
            val intent = Intent(this, ActualizarUserNameActivity::class.java)
            startActivity(intent)
        }

        binding.actualizarPassword.myButtonOption.text = "Password"
        binding.actualizarPassword.myButtonOption.setOnClickListener {
            val intent = Intent(this, ActualizarPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    //evento volver atras toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun navigateActualizarUserName(){

    }

    private fun navigateActualizarPassword(){

    }
}