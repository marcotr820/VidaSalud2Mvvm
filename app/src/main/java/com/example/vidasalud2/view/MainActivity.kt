package com.example.vidasalud2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.vidasalud2.databinding.ActivityMainBinding
import com.example.vidasalud2.model.LoginModel
import com.example.vidasalud2.utils.ProgressLoading
import com.example.vidasalud2.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityMainBinding

    //progress dialog
    private lateinit var progressDialog: ProgressLoading

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressLoading(this)

        binding.btnLogin.setOnClickListener {
            loginMainActivity()
        }

        mainViewModel.isloading.observe(this, Observer { isLoading ->
            progressDialog.mostrarDialog(isLoading)
        })

        mainViewModel.resp.observe(this, Observer {result ->
            showToast("${result}")
        })

        mainViewModel.username.observe(this, Observer { message ->
            if (message.isNullOrBlank()){
                binding.userNameContainer.error = null
            } else {
                binding.userNameContainer.error = message
            }
        })
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
}