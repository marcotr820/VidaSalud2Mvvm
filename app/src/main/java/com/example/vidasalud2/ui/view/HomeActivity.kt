package com.example.vidasalud2.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.vidasalud2.R
import com.example.vidasalud2.core.dataStore
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.databinding.ActivityHomeBinding
import com.example.vidasalud2.ui.fragments.CuentaFragment
import com.example.vidasalud2.ui.fragments.HomeFragment
import com.example.vidasalud2.ui.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    //datastore
    //private val dataStoreViewModel: DataStoreViewModel by viewModels()

    //viewBinding
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configuracion bottoNav
        val navController = findNavController(R.id.hostFragment)
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

//        replaceFragment(HomeFragment())
//
//        binding.bottomNav.setOnItemSelectedListener {itemSelected ->
//            when(itemSelected.itemId) {
//                R.id.actionHome -> replaceFragment(HomeFragment())
//                R.id.actionCuenta -> replaceFragment(CuentaFragment())
//                else -> {}
//            }
//            true
//        }
    }

//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.hostFragment, fragment)
//            .commit()
//    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}