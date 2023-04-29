package com.example.vidasalud2.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.vidasalud2.R
import com.example.vidasalud2.databinding.ActivityHomeBinding
import com.example.vidasalud2.ui.fragments.CuentaFragment
import com.example.vidasalud2.ui.fragments.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    //datastore
    //private val dataStoreViewModel: DataStoreViewModel by viewModels()

    //viewBinding
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: controlar el ciclo de vida de activity para que cuando se cambie de tema o cambie
        // la posicion de la pantalla el fragment no se vuelva a crear si savedInstanceState es null
        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.hostFragment, fragment)
            transaction.commit()
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {itemSelected ->
            when(itemSelected.itemId) {
                R.id.actionHome -> replaceFragment(HomeFragment())
                R.id.actionCuenta -> replaceFragment(CuentaFragment())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.hostFragment, fragment)
        transaction.commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}