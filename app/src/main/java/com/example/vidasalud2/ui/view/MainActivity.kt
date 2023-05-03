package com.example.vidasalud2.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.vidasalud2.databinding.ActivityMainBinding
import com.example.vidasalud2.data.model.LoginModel
import com.example.vidasalud2.domain.UseCases.FieldValidation.password.ValidatePasswordUseCase
import com.example.vidasalud2.domain.UseCases.FieldValidation.username.ValidateUserNameUseCase
import com.example.vidasalud2.ui.viewmodel.DataStoreViewModel
import com.example.vidasalud2.utils.ProgressLoading
import com.example.vidasalud2.ui.viewmodel.MainViewModel
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

        binding.btnLogin.setOnClickListener {
            loginMainActivity()
        }

        mainViewModel.resp.observe(this, Observer {result ->
            //showToast("${result}")
            if (result.error.isNullOrBlank()) {
                dataStoreViewModel.setIsLoggedIn(true)
                dataStoreViewModel.saveToken(result.dataResult?.token.orEmpty())
                dataStoreViewModel.saveUser(result.dataResult?.usuario!!)
                navigateHome()
            }
        })

        mainViewModel.isloading.observe(this, Observer { isLoading ->
            progressDialog.mostrarDialog(isLoading)
        })

        binding.btnRegistro.setOnClickListener { navigateRegistro() }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun loginMainActivity() {
        val username = binding.inputUsuario.text.toString().trim()
        val password = binding.inputPassword.text.toString().trim()
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
        finish()
    }
}