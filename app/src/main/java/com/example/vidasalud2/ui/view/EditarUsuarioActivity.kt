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
import com.example.vidasalud2.ui.adapter.RolAdapter
import com.example.vidasalud2.ui.viewmodel.EditarUsuarioViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarUsuarioBinding

    private val editarUsuarioViewModel: EditarUsuarioViewModel by viewModels()

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
        val usuario = Gson().fromJson(usuarioJson, Usuario::class.java)

        binding.usuariotv.text = usuario.toString()
        binding.correotv.text = usuario.email

        //obtenemos los roles para el dropdown
        editarUsuarioViewModel.rolesDropdownLiveData.observe(this) {listRoles ->
            setRolesDropdown(listRoles)
        }

        editarUsuarioViewModel.msgToastLiveData.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

        editarUsuarioViewModel.rolLiveData.observe(this) {
            if (it.isSuccess){
                binding.rolDropdownContainer.error = null
            } else {
                binding.rolDropdownContainer.error = it.errorMessage
            }
        }

        binding.actualizarUsuarioBtn.setOnClickListener { actualizarUsuario() }

        //evento dropdown selected
        binding.rolesDropdown.setOnItemClickListener { parent, view, position, id ->
            val rol = parent.adapter.getItem(position) as Rol
            Log.d("gg", "${rol.id}")
            // Realizar las acciones necesarias con el usuario seleccionado
        }

        //evento spinner selected
        binding.rolspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position)
                Toast.makeText(this@EditarUsuarioActivity, "Seleccionaste $item", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se seleccionó ningún item
            }
        }
    }

    private fun actualizarUsuario() {
        editarUsuarioViewModel.actualizarUsuario()
    }

    //evento volver atras toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setRolesDropdown(items: List<Rol>){
        //dropdown
        val adapter = ArrayAdapter(this, R.layout.my_item_dropdown, items)
        binding.rolesDropdown.setAdapter(adapter)
        val rolSeleccionado = items.find { it.id == "cc4287e5-8d42-49dc-93c5-a0d3997e1937" }
        binding.rolesDropdown.setText(rolSeleccionado?.name, false)

        //spinner
        val adapterspinner = RolAdapter(this, items)
        binding.rolspinner.adapter = adapterspinner
        val itemselected = items.indexOfFirst { it.id == "cc4287e5-8d42-49dc-93c5-a0d3997e1937" }
        binding.rolspinner.setSelection(itemselected)
    }
}