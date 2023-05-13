package com.example.vidasalud2.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.databinding.ActivityUsuariosBinding
import com.example.vidasalud2.ui.adapter.UsuarioAdapter
import com.example.vidasalud2.ui.viewmodel.UsuarioViewModel
import com.example.vidasalud2.utils.CheckInternetConnection
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UsuariosActivity : AppCompatActivity() {

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection

    private lateinit var binding: ActivityUsuariosBinding

    private lateinit var _usuariosLista: List<Usuario>

    private lateinit var adapter: UsuarioAdapter

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
                _usuariosLista = it.dataResult.orEmpty()
                initRecyclerView(_usuariosLista)
        })

        usuarioViewModel.isloadingLiveData.observe(this, Observer {
            if (it) {
                binding.progressbar.visibility = View.VISIBLE
            } else {
                binding.progressbar.visibility = View.GONE
            }
        })

        usuarioViewModel.msgToastLiveData.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        //Get All usuarios
        usuarioViewModel.getUsuarios()
    }

    //toolbar menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.buscarUsuarioAction)
        val searchView = searchItem?.actionView as SearchView

//        searchView.setOnSearchClickListener {
//            showToast("abierto")
//        }
//        searchView.setOnCloseListener {
//            true
//        }

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                for (i in 0 until menu.size()) {
                    val menuItem = menu.getItem(i)
                    if (menuItem.itemId != R.id.buscarUsuarioAction) {
                        menuItem.isVisible = false
                    }
                }
                return true
            }
            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                for (i in 0 until menu.size()) {
                    val menuItem = menu.getItem(i)
                    menuItem.isVisible = true
                }
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.buscarUsuario( newText.orEmpty() )
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {  //evento option <-back
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.agregarUsuarioAction -> {
                navigateToAgregarUsuario()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToAgregarUsuario() {
        val intent = Intent(this, AgregarUsuarioActivity::class.java)
        startActivity(intent)
    }

    private fun initRecyclerView(usuariosLista: List<Usuario>) {
        adapter = UsuarioAdapter(
            _usuariosOriginalList = usuariosLista,
            _onClickListener = { usuario -> usuarioSeleccionado(usuario) })
        binding.usuariosRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.usuariosRecyclerView.adapter = adapter
    }

    private fun usuarioSeleccionado(usuario: Usuario) {
        if (checkInternetConnection()) {
            val intent = Intent(this, EditarUsuarioActivity::class.java)
            intent.putExtra("usuario", Gson().toJson(usuario))
            startActivity(intent)
        } else {
            showToast("Sin internet")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}