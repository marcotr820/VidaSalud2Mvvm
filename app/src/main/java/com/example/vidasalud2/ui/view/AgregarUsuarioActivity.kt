package com.example.vidasalud2.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.vidasalud2.R
import com.example.vidasalud2.databinding.ActivityAgregarUsuarioBinding
import com.google.android.material.appbar.MaterialToolbar

class AgregarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configuraciones toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.agregarUsuarioToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = "Agregar Usuario"
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {  //evento option <-back
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}