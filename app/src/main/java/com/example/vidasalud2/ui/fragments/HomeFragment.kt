package com.example.vidasalud2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.auth0.jwt.JWT
import com.example.vidasalud2.databinding.FragmentHomeBinding
import com.example.vidasalud2.ui.viewmodel.DataStoreViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //datastore
    private val dataStoreViewModel: DataStoreViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
//        binding.btnGetToken.setOnClickListener {
//            showToast(dataStoreViewModel.getToken().toString())
//
//            val logged = dataStoreViewModel.getIsLoggedIn()
//            showToast("loginYOO: $logged")
//
////            val isLoggedIn = dataStore.data.map { preferences ->
////                preferences[booleanPreferencesKey(DataStorePreferencesKeys.LOGGEDIN)] ?: false
////            }
////            lifecycleScope.launch(Dispatchers.IO) {
////                isLoggedIn.collect {isLogged ->
////                    withContext(Dispatchers.Main) {
////                        if (isLogged) {
////                            showToast("login: $isLogged")
////                        }
////                    }
////                }
////            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    //TODO: Por lo tanto, cualquier código que configure o interactúe con la vista del Fragment debe
    // ser colocado dentro del método onViewCreated(). Esto incluye la configuración de los widgets
    // de la vista, la asignación de controladores de eventos y la ejecución de cualquier otra tarea
    // relacionada con la vista.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}