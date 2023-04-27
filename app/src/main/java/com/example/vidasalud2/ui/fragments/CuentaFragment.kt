package com.example.vidasalud2.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.auth0.jwt.JWT
import com.example.vidasalud2.R
import com.example.vidasalud2.databinding.FragmentCuentaBinding
import com.example.vidasalud2.ui.view.MainActivity
import com.example.vidasalud2.ui.viewmodel.DataStoreViewModel
import com.example.vidasalud2.utils.Constants
import com.example.vidasalud2.utils.MyAdapter


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

        val username = dataStoreViewModel.getUser()?.userName.orEmpty()
        _binding.tvUserName.text = username

        val email = dataStoreViewModel.getUser()?.email.orEmpty()
        _binding.tvEmail.text = email

        val token = dataStoreViewModel.getToken().orEmpty()

        val decodedJWT = JWT.decode(token)
        val role = decodedJWT.getClaim(Constants.role).asString()

        if (role == Constants.SUPERADMIN) {
            _binding.listCuentasContainer.visibility = View.VISIBLE
        }

        //val listCuentasAdapter: ArrayAdapter<String>

        val listCuentas = listOf(
            "Gestionar Usuarios",
            "Gestionar Roles"
        )

        //listCuentasAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listCuentas)
        var adapter = MyAdapter(requireContext(), listCuentas)
        _binding.listCuentas.adapter = adapter

        _binding.btnLogout.setOnClickListener {
            dataStoreViewModel.clearDataStore()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}