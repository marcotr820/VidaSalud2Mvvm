package com.example.vidasalud2.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.auth0.jwt.JWT
import com.example.vidasalud2.R
import com.example.vidasalud2.databinding.FragmentCuentaBinding
import com.example.vidasalud2.ui.view.ConfiguracionCuentaActivity
import com.example.vidasalud2.ui.view.MainActivity
import com.example.vidasalud2.ui.view.RolesActivity
import com.example.vidasalud2.ui.view.UsuariosActivity
import com.example.vidasalud2.ui.viewmodel.DataStoreViewModel
import com.example.vidasalud2.utils.Constants


class CuentaFragment : Fragment() {

    private lateinit var _binding: FragmentCuentaBinding

    //datastore
    private val dataStoreViewModel: DataStoreViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCuentaBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(_binding.cuentaToolbar.toolbarLayout)
        (requireActivity() as AppCompatActivity).supportActionBar?.let {
            it.title = "Cuenta"
        }

        val username = dataStoreViewModel.getUser()?.userName.orEmpty()
        _binding.tvUserName.text = username

        val email = dataStoreViewModel.getUser()?.email.orEmpty()
        _binding.tvEmail.text = email

        val token = dataStoreViewModel.getToken().orEmpty()
        val decodedJWT = JWT.decode(token)
        val role = decodedJWT.getClaim(Constants.role).asString()
        if (role == Constants.SUPERADMIN) {
            _binding.gestionCuentasContainer.visibility = View.VISIBLE
        }

        _binding.btnUsuarios.myButtonOption.text = getString(R.string.UsuariosOption)
        _binding.btnUsuarios.myButtonOption.setOnClickListener {
            val intent = Intent(requireContext(), UsuariosActivity::class.java)
            startActivity(intent)
        }

        _binding.btnRoles.myButtonOption.text = getString(R.string.RolesOption)
        _binding.btnRoles.myButtonOption.setOnClickListener {
            val intent = Intent(requireContext(), RolesActivity::class.java)
            startActivity(intent)
        }

        _binding.btnConfiguracionCuenta.myButtonOption.text = getString(R.string.ConfiguracionCuenta)
        _binding.btnConfiguracionCuenta.myButtonOption.setOnClickListener {
            val intent = Intent(requireContext(), ConfiguracionCuentaActivity::class.java)
            startActivity(intent)
        }

        _binding.btnLogout.setOnClickListener {
            val alertaDialog = AlertDialog.Builder(requireContext())
            alertaDialog.apply {
                //setCancelable(false)
                setTitle("Cerrar Sesión")
                setMessage("Esta Seguro de Cerrar Sesión?")
                setPositiveButton("cerrar sesión"){ _, _ ->
                    cerrarSesion()
                }
                setNegativeButton(android.R.string.cancel, null)
            }.create().show()

        }

    }

    private fun cerrarSesion() {
            dataStoreViewModel.clearDataStore()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
    }
}