package com.example.vidasalud2.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.vidasalud2.R
import com.example.vidasalud2.data.DataStore.DataStorePreferencesKeys
import com.example.vidasalud2.data.DataStore.DataStoreRepositoryManager
import com.example.vidasalud2.databinding.ActivityActualizarUserNameBinding
import com.example.vidasalud2.ui.viewmodel.ActualizarUserNameViewModel
import com.example.vidasalud2.utils.ProgressLoading
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ActualizarUserNameActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreRepositoryManager: DataStoreRepositoryManager

    private lateinit var binding: ActivityActualizarUserNameBinding

    private val actualizarUserNameViewModel: ActualizarUserNameViewModel by viewModels()

    private val progressLoading = ProgressLoading(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<MaterialToolbar>(R.id.actualizarUserNameToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let{
            it.title = "Actualizar username"
            it.setDisplayHomeAsUpEnabled(true)
        }

        val username = runBlocking {
            dataStoreRepositoryManager.getUser(DataStorePreferencesKeys.USER)
        }
        binding.tvusernameActual.text = username?.userName

        binding.actualizarUserNameBtn.setOnClickListener {
            actualizarUserName()
        }

        actualizarUserNameViewModel.userNameLiveData.observe(this, Observer {
            if (it.isSuccess){
                binding.userNameContainer.error = null
            } else {
                binding.userNameContainer.error = it.errorMessage
            }
        })

        actualizarUserNameViewModel.msgToastLiveData.observe(this, Observer { toastMessage ->
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        })

        actualizarUserNameViewModel.isloadingLiveData.observe(this, Observer {
            progressLoading.mostrarDialog(it)
        })

        actualizarUserNameViewModel.actualizarUserNameIsSuccessLiveData.observe(this){
            if (it){ navigateToLogin() }
        }
    }

    private fun actualizarUserName() {
        val userName = binding.userNameTi.text.toString().trim()
        actualizarUserNameViewModel.actualizarUserName(userName)
    }

    //evento volver atras toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun navigateToLogin(){
        val intent = Intent(this, MainActivity::class.java)
        //limpiamos la pila de activiy
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}