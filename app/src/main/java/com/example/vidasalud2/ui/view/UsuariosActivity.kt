package com.example.vidasalud2.ui.view

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.databinding.ActivityUsuariosBinding
import com.example.vidasalud2.ui.adapter.UsuarioAdapter
import com.example.vidasalud2.ui.viewmodel.UsuarioViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsuariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsuariosBinding

    //MainViewmodel
    private val viewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configuraciones toolbar
        val toolbar = binding.usuarioToolbar.toolbarLayout
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = "Usuarios"
            it.setDisplayHomeAsUpEnabled(true)
        }

        //obtenemos los usuarios
        viewModel.getUsuarios()

        viewModel.usuarios.observe(this, Observer {
            if (it.error.isNullOrBlank()){
                initRecyclerView(it.dataResult ?: emptyList())
            }
        })

        viewModel.isloading.observe(this, Observer {
            if (it) {
                binding.progressbar.visibility = View.VISIBLE
            } else {
                binding.progressbar.visibility = View.GONE
            }
        })
    }

    //opcion por defecto menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //evento volver atras
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun initRecyclerView(usuariosLista: List<Usuario>) {
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, manager.orientation)
        binding.usuariosRecyclerView.layoutManager = manager
        binding.usuariosRecyclerView.adapter = UsuarioAdapter(usuariosLista)
        binding.usuariosRecyclerView.addItemDecoration(decoration)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}