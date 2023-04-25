package com.example.vidasalud2.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.example.vidasalud2.core.dataStore
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.databinding.ActivityHomeBinding
import com.example.vidasalud2.ui.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    //datastore
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    //viewBinding
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetToken.setOnClickListener {
            showToast(dataStoreViewModel.getToken().toString())

            val logged = dataStoreViewModel.getIsLoggedIn()
            showToast("loginYOO: $logged")

//            val isLoggedIn = dataStore.data.map { preferences ->
//                preferences[booleanPreferencesKey(DataStorePreferencesKeys.LOGGEDIN)] ?: false
//            }
//            lifecycleScope.launch(Dispatchers.IO) {
//                isLoggedIn.collect {isLogged ->
//                    withContext(Dispatchers.Main) {
//                        if (isLogged) {
//                            showToast("login: $isLogged")
//                        }
//                    }
//                }
//            }
        }

        binding.btnGetUser.setOnClickListener {
            showToast("${dataStoreViewModel.getUser()}")
            showToast("${dataStoreViewModel.getUser()?.userName.orEmpty()}")
            showToast("${dataStoreViewModel.getUser()?.email.orEmpty()}")
        }

        binding.btnLogout.setOnClickListener {
            dataStoreViewModel.clearDataStore()
            val intent = Intent(application, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}