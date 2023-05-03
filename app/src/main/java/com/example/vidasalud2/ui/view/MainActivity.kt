package com.example.vidasalud2.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.databinding.ActivityMainBinding
import com.example.vidasalud2.ui.viewmodel.DataStoreViewModel
import com.example.vidasalud2.ui.viewmodel.MainViewModel
import com.example.vidasalud2.utils.ProgressLoading
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityMainBinding

    //progress dialog
    private lateinit var progressDialog: ProgressLoading

    //MainViewmodel
    private val mainViewModel: MainViewModel by viewModels()

    //datastoreViewModel
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val estalogueado = dataStoreViewModel.getIsLoggedIn()
        if (estalogueado) {
            val intent = Intent(application, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        progressDialog = ProgressLoading(this)

        binding.btnLogin.setOnClickListener { loginMainActivity() }

        mainViewModel.userNameLiveData.observe(this, Observer { observeLiveData(it, binding.userNameContainer) })

        mainViewModel.passwordLiveData.observe(this, Observer { observeLiveData(it, binding.passwordContainer) })

        mainViewModel.loginSuccessLiveData.observe(this, Observer { loginExitoso ->
            if (loginExitoso) { navigateHome() }
        })

        mainViewModel.msgToastLiveData.observe(this, Observer {msgText ->
            msgText?.let {
                Toast.makeText(this, msgText, Toast.LENGTH_SHORT).show()
                mainViewModel.msgToastNull()
            }
        })

        mainViewModel.isloadingLiveData.observe(this, Observer { isLoading ->
            progressDialog.mostrarDialog(isLoading)
        })

        binding.btnRegistro.setOnClickListener { navigateRegistro() }
    }

    private fun loginMainActivity() {
        val username = binding.userNameInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()
        val logindto = LoginModel(username, password)

        mainViewModel.login(logindto)
    }

    private fun navigateHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateRegistro() {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }

    private fun observeLiveData(resultField: ValidateResultField, containerLayout: TextInputLayout){
        if (resultField.isSuccess){
            containerLayout.error = null
        }else {
            containerLayout.error = resultField.errorMessage
        }
    }
}