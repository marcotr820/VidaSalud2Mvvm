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
import com.example.vidasalud2.ui.adapter.SpinnerAdapter
import com.example.vidasalud2.ui.viewmodel.EditarUsuarioViewModel
import com.example.vidasalud2.utils.ProgressLoading
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarUsuarioBinding

    private val editarUsuarioViewModel: EditarUsuarioViewModel by viewModels()

    private val progressLoading = ProgressLoading(this)

    private lateinit var usuarioData: Usuario

    private var spinnerRolSelected: Boolean = false
    private var spinnerBloqueoSelected: Boolean = false

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

        //llamada getRoles
        editarUsuarioViewModel.getRolesDropdown()

        val usuarioJson = intent.extras?.getString("usuario")
        usuarioData = Gson().fromJson(usuarioJson, Usuario::class.java)

        //si se usa los datos de usuario se deben declarar despues de que este inicializado
        setSpinnerBloqueoOptions()

        binding.usuariotv.text = usuarioData.userName
        binding.correotv.text = usuarioData.email

        //obtenemos los roles para el dropdown
        editarUsuarioViewModel.rolesDropdownLiveData.observe(this) {listRoles ->
            setRolesDropdown(listRoles)
        }

        editarUsuarioViewModel.msgToastLiveData.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

        editarUsuarioViewModel.isloadingLiveData.observe(this) {
            progressLoading.mostrarDialog(it)
        }

        binding.rolSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (spinnerRolSelected){
                    val rolSelected = parent.getItemAtPosition(position) as Rol
                    actualizarRolUsuario(rolSelected)
                } else { spinnerRolSelected = true }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Acciones a realizar cuando no se selecciona ningún elemento
            }
        }

        binding.bloqueoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (spinnerBloqueoSelected){
                    cambiarEstadoBloqueo(usuarioData.id)
                } else { spinnerBloqueoSelected = true }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Acciones a realizar cuando no se selecciona ningún elemento
            }
        }
    }

    private fun cambiarEstadoBloqueo(usuarioId: String){
        editarUsuarioViewModel.cambiarEstadoBloqueo(usuarioId)
    }

    private fun setSpinnerBloqueoOptions() {
        val bloqueoOptions = resources.getStringArray(R.array.SpinnerBloqueoOptions)
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, bloqueoOptions)
        binding.bloqueoSpinner.adapter = adapter
        if (usuarioData.isBlocked){
            binding.bloqueoSpinner.setSelection(0)  //siguiendo la posicion de SpinnerBloqueoOptions
        }else {
            binding.bloqueoSpinner.setSelection(1)
        }
    }

    private fun actualizarRolUsuario(rolSelected: Rol) {
        editarUsuarioViewModel.setSelectedRolId(rolSelected.id)
        editarUsuarioViewModel.actualizarRolUsuario(usuarioData)
    }

    //evento volver atras toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setRolesDropdown(items: List<Rol>){
        val adapterspinner = SpinnerAdapter(items)
        binding.rolSpinner.adapter = adapterspinner
        val itemselected = items.indexOfFirst { it.id == usuarioData.rol?.first() }
        binding.rolSpinner.setSelection(itemselected)
    }

}