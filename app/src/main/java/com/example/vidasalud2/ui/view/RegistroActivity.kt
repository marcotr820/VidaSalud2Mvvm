package com.example.vidasalud2.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.databinding.ActivityRegistroBinding
import com.example.vidasalud2.ui.viewmodel.DataStoreViewModel
import com.example.vidasalud2.ui.viewmodel.RegistroViewModel
import com.example.vidasalud2.utils.ProgressLoading
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    //progress dialog
    private lateinit var progressDialog: ProgressLoading

    //registroviewmodel
    private val registroViewModel: RegistroViewModel by viewModels()

    //datastoreViewModel
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressLoading(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.registroToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = "Registrarse"
            it.setDisplayHomeAsUpEnabled(true)
        }

        binding.registrarseBtn.setOnClickListener { registrarse() }

        registroViewModel.isloadingLiveData.observe(this, Observer { isLoading ->
            progressDialog.mostrarDialog(isLoading)
        })

        registroViewModel.userNameLiveData.observe(this, Observer {
            observeLiveData(it, binding.userNameContainer)
        })

        registroViewModel.emailLiveData.observe(this, Observer {
            observeLiveData(it, binding.emailContainer)
        })

        registroViewModel.passwordLiveData.observe(this, Observer {
            observeLiveData(it, binding.passwordContainer)
        })

        registroViewModel.repeatPasswordLiveData.observe(this, Observer {
            observeLiveData(it, binding.repetirpasswordContainer)
        })

        registroViewModel.registroSuccessLiveData.observe(this, Observer {registoExitoso ->
            if (registoExitoso) { redirectHome() }
        })

        registroViewModel.msgToastLiveData.observe(this, Observer {msgText ->
            msgText?.let {
                Toast.makeText(this, msgText, Toast.LENGTH_SHORT).show()
                registroViewModel.msgToastNull()
            }
        })

    }

    //evento volver atras
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun registrarse() {
        val username = binding.userNameInput.text.toString().trim()
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()
        val repeatPassword = binding.repetirpasswordInput.text.toString().trim()
        registroViewModel.registrarse(username, email, password, repeatPassword)
    }

    private fun observeLiveData(resultField: ValidateResultField, containerLayout: TextInputLayout){
        if (resultField.isSuccess){
            containerLayout.error = null
        }else {
            containerLayout.error = resultField.errorMessage
        }
    }

    private fun redirectHome(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        //finishAffinity()    //elimina la pila de actividades para no regresar atras
    }
}