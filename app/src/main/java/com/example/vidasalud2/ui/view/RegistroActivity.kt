package com.example.vidasalud2.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.vidasalud2.databinding.ActivityRegistroBinding
import com.example.vidasalud2.domain.UseCases.FieldValidation.username.ValidateUserNameUseCase
import com.example.vidasalud2.ui.viewmodel.RegistroViewModel
import com.example.vidasalud2.usuarios.UsuarioRepository
import com.example.vidasalud2.utils.ProgressLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    //progress dialog
    private lateinit var progressDialog: ProgressLoading

    private val registroViewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressLoading(this)

        val toolbar = binding.registroToolbar.toolbarLayout
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            //it.title = "Registrarse"
            it.setDisplayHomeAsUpEnabled(true)
        }

        binding.registrarseBtn.setOnClickListener { registroViewModel.registrarse() }

        registroViewModel.isloading.observe(this, Observer { isLoading ->
            progressDialog.mostrarDialog(isLoading)
        })

//        registroViewModel.buttonIsValid.observe(this, Observer {isValid ->
//            binding.registrarseBtn.isEnabled = isValid
//        })

        binding.userNameInput.addTextChangedListener(object : TextWatcher {
            private var validationJob: Job? = null

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // No se requiere ninguna acción antes de que cambie el texto
                var usuarioProgress = binding.usernameProgressbar
                if ( usuarioProgress.visibility != View.VISIBLE ){
                    usuarioProgress.visibility = View.VISIBLE
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // No se requiere ninguna acción cuando cambia el texto
            }

            override fun afterTextChanged(dataText: Editable?) {
                validationJob?.cancel() // Cancelar el trabajo anterior si aún está en progreso
                validationJob = CoroutineScope(Dispatchers.IO).launch {
                    delay(1200)
                    //validar username
                    registroViewModel.validarUserName(dataText.toString())
                    withContext(Dispatchers.Main) {
                        // Realizar alguna acción con el resultado de la validación en el hilo principal
                        binding.usernameProgressbar.visibility = View.INVISIBLE
                        //registroViewModel.buttonHabilitado()
                    }

                }
            }
        })

        registroViewModel.usernameField.observe(this, Observer {
            if (it.isSuccess) {
                binding.userNameContainer.isErrorEnabled = false
            } else {
                binding.userNameContainer.error = it.errorMessage
            }
        })

        binding.emailInput.addTextChangedListener(object : TextWatcher {
            private var validationJob: Job? = null

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // No se requiere ninguna acción antes de que cambie el texto
                var usuarioProgress = binding.emailProgressbar
                if ( usuarioProgress.visibility != View.VISIBLE ){
                    usuarioProgress.visibility = View.VISIBLE
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // No se requiere ninguna acción cuando cambia el texto
            }

            override fun afterTextChanged(dataText: Editable?) {
                validationJob?.cancel() // Cancelar el trabajo anterior si aún está en progreso
                validationJob = CoroutineScope(Dispatchers.IO).launch {
                    delay(1200)
                    //validar email
                    registroViewModel.validarEmail(dataText.toString())
                    withContext(Dispatchers.Main) {
                        // Realizar alguna acción con el resultado de la validación en el hilo principal
                        binding.emailProgressbar.visibility = View.INVISIBLE
                        //registroViewModel.buttonHabilitado()
                    }
                }
            }
        })

        registroViewModel.emailField.observe(this, Observer {
            if (it.isSuccess) {
                binding.emailContainer.isErrorEnabled = false
            } else {
                binding.emailContainer.error = it.errorMessage
            }
        })

        registroViewModel.catchError.observe(this, Observer {catchErrorText ->
            catchErrorText?.let {
                Toast.makeText(this, catchErrorText, Toast.LENGTH_SHORT).show()
                registroViewModel.catchErrorNull()
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
    }
}