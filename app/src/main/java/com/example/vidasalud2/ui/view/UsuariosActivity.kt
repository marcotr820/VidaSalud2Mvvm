package com.example.vidasalud2.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
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
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsuariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsuariosBinding

    //MainViewmodel
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configuraciones toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.usuarioToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = "Usuarios"
            it.setDisplayHomeAsUpEnabled(true)
        }

        usuarioViewModel.usuarios.observe(this, Observer {
            initRecyclerView(it.dataResult ?: emptyList())
        })

        usuarioViewModel.isloadingLiveData.observe(this, Observer {
            if (it) {
                binding.progressbar.visibility = View.VISIBLE
            } else {
                binding.progressbar.visibility = View.GONE
            }
        })

        usuarioViewModel.msgToast.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        //peticon cada vez que se acceda al activity
        usuarioViewModel.getUsuarios()
    }

    //toolbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //evento volver atras toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun initRecyclerView(usuariosLista: List<Usuario>) {
        //val manager = LinearLayoutManager(this)
        //val decoration = DividerItemDecoration(this, manager.orientation)
        binding.usuariosRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.usuariosRecyclerView.adapter = UsuarioAdapter(usuariosLista) { usuario ->
            usuarioSeleccionado(
                usuario
            )
        }
    }

    private fun usuarioSeleccionado(usuario: Usuario) {
        val intent = Intent(this, EditarUsuarioActivity::class.java)
        intent.putExtra("usuario", Gson().toJson(usuario))
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}