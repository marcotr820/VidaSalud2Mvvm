package com.example.vidasalud2.ui

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.kotlinsample.helpers.ViewBindingKotlinModel
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.Rol
import com.example.vidasalud2.data.model.Usuario
import com.example.vidasalud2.databinding.MyUsuarioDetailBinding
import com.example.vidasalud2.ui.adapter.SpinnerAdapter
import com.example.vidasalud2.ui.epoxy.LoadingEpoxyModel
import com.example.vidasalud2.ui.viewmodel.EditarUsuarioViewModel
import com.example.vidasalud2.utils.Constants
import javax.inject.Inject

class UsuarioDetalleEpoxyController(
    private val context: Context,
    private val viewmodel: EditarUsuarioViewModel
): EpoxyController() {

    private var _usuario: Usuario? = null
    private var _roles: List<Rol> = emptyList()
    private var _rolStatus: Boolean = false //evitar el primer cambio de rol por defecto
    private var _bloqueoStatus: Boolean = false //evitar el primer cambio de bloqueo por defecto

    var isLoading: Boolean = true
        set(value) {
            field = value
            if (field){
                requestModelBuild()
            }
        }

    fun setData(usuario: Usuario?, roles: List<Rol>){
        _usuario = usuario
        _roles = roles
        if (_usuario != null && _roles.isNotEmpty()) {
            isLoading = false
            requestModelBuild()
        }
    }

    override fun buildModels() {

        if (isLoading){
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }

        if (_usuario == null || _roles.isEmpty()){
            return
        }

        //agregamos los datos la vista mediante los metodos
        UsuarioDatosEpoxyModel(_usuario!!, _roles,
            viewmodel, context, _rolStatus, _bloqueoStatus).id("header").addTo(this)
    }

    data class UsuarioDatosEpoxyModel(
        val usuario: Usuario,
        val listaRoles: List<Rol>,
        val editarviewmodel: EditarUsuarioViewModel,
        val context: Context,
        var rolStatus: Boolean,
        var bloqueoStatus: Boolean
    ): ViewBindingKotlinModel<MyUsuarioDetailBinding>(R.layout.my_usuario_detail){
        override fun MyUsuarioDetailBinding.bind() {
            usernametv.text = usuario.userName
            emailtv.text = usuario.email

            //spinner roles
            val adapterspinner = SpinnerAdapter(listaRoles)
            spinnerRoles.adapter = adapterspinner
            val itemselected = listaRoles.indexOfFirst { it.id == usuario.rol!![0] }
            spinnerRoles.setSelection(itemselected)
            spinnerRoles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if (rolStatus){
                        val rolSelected = parent.getItemAtPosition(position) as Rol
                        editarviewmodel.setSelectedRolId(rolSelected.id)
                        editarviewmodel.actualizarRolUsuario(usuario)
                    } else { rolStatus = true }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Acciones a realizar cuando no se selecciona ningún elemento
                }
            }

            //spinner bloqueo
            val bloqueoOptions = Constants.opcionesBloqueo
            val adapter = ArrayAdapter(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, bloqueoOptions)
            spinnerEstadoUsuario.adapter = adapter
            if (usuario.isBlocked){
                spinnerEstadoUsuario.setSelection(0) //siguiendo la posicion del array
            } else { spinnerEstadoUsuario.setSelection(1) }
            spinnerEstadoUsuario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if (bloqueoStatus){
                        editarviewmodel.cambiarEstadoBloqueo(usuario.id)
                    } else { bloqueoStatus = true }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Acciones a realizar cuando no se selecciona ningún elemento
                }
            }

            //eliminar usuario btn
            eliminarUsuarioBtn.setOnClickListener {
                val alertaDialog = AlertDialog.Builder(context)
                alertaDialog.apply {
                    //setCancelable(false)
                    setTitle("Eliminar usuario")
                    setMessage("Esta Seguro de Eliminar este usuario?")
                    setPositiveButton("aceptar"){ _, _ ->
                        editarviewmodel.eliminarUsuario(usuario.id)
                    }
                    setNegativeButton(android.R.string.cancel, null)
                }.create().show()
            }
        }
    }
}