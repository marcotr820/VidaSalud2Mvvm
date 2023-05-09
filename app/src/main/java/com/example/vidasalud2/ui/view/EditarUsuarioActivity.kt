package com.example.vidasalud2.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.Rol
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.databinding.ActivityEditarUsuarioBinding
import com.example.vidasalud2.ui.UsuarioDetalleEpoxyController
import com.example.vidasalud2.ui.adapter.SpinnerAdapter
import com.example.vidasalud2.ui.viewmodel.EditarUsuarioViewModel
import com.example.vidasalud2.utils.CheckInternetConnection
import com.example.vidasalud2.utils.ProgressLoading
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarUsuarioBinding

    private val editarUsuarioViewModel: EditarUsuarioViewModel by viewModels()

    //epoxy
    private lateinit var usuarioDetalleEpoxyController: UsuarioDetalleEpoxyController

    private lateinit var usuarioData: Usuario

    private val progressLoading = ProgressLoading(this)

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

        //obtener roles
        editarUsuarioViewModel.getRolesDropdown()

        val usuarioJson = intent.extras?.getString("usuario")
        usuarioData = Gson().fromJson(usuarioJson, Usuario::class.java)

        //obtenemos los roles para el dropdown
        editarUsuarioViewModel.rolesDropdownLiveData.observe(this) {listaRoles ->
            usuarioDetalleEpoxyController.setData(usuarioData, listaRoles)
        }

        editarUsuarioViewModel.msgToastLiveData.observe(this) {
            showToast(it.toString())
        }

        editarUsuarioViewModel.isloadingLiveData.observe(this) {
            progressLoading.mostrarDialog(it)
        }

        editarUsuarioViewModel.eliminarUsuarioLiveData.observe(this) {
            if (it) { navigateListaUsuarios() }
        }

        usuarioDetalleEpoxyController = UsuarioDetalleEpoxyController(this, editarUsuarioViewModel)
        binding.epoxyEditarUsuario.setControllerAndBuildModels(usuarioDetalleEpoxyController)
    }

    fun navigateListaUsuarios(){
        onBackPressedDispatcher.onBackPressed()
    }

    //evento volver atras toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}