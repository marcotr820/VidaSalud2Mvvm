package com.example.vidasalud2.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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

    //
    private lateinit var validateUserNameUseCase: ValidateUserNameUseCase

    @Inject
    lateinit var usuarioRepository: UsuarioRepository

    private val registroViewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressLoading(this)

        //
        validateUserNameUseCase = ValidateUserNameUseCase(usuarioRepository)

        val toolbar = binding.registroToolbar.toolbarLayout
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            //it.title = "Registrarse"
            it.setDisplayHomeAsUpEnabled(true)
        }

        //binding.registrarseBtn.setOnClickListener { registrarse() }

        registroViewModel.isloading.observe(this, Observer { isLoading ->
            progressDialog.mostrarDialog(isLoading)
        })

        binding.userNameInput.addTextChangedListener(object : TextWatcher {
            private var validationJob: Job? = null

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // No se requiere ninguna acción antes de que cambie el texto
                if (binding.usernameProgressbar.visibility != View.VISIBLE ){
                    Log.d("marco", "before")
                    binding.usernameProgressbar.visibility = View.VISIBLE
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // No se requiere ninguna acción cuando cambia el texto
            }

            override fun afterTextChanged(p0: Editable?) {
                validationJob?.cancel() // Cancelar el trabajo anterior si aún está en progreso
                validationJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(1000) // Pausar la ejecución durante 500 milisegundos
                    Log.d("marco", p0.toString()) // Realizar la tarea después de la pausa
                    validarCampos(p0.toString())
                    binding.usernameProgressbar.visibility = View.INVISIBLE
                }
            }
        })

        registroViewModel.usuarioField.observe(this, Observer {
            if (it.isSuccess) {
                binding.userNameContainer.helperText = null
            } else {
                binding.userNameContainer.error = it.errorMessage
            }
        })

        registroViewModel.emailField.observe(this, Observer {
            if (it.isSuccess) {
                binding.emailContainer.helperText = ""
            } else {
                binding.emailContainer.error = it.errorMessage
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

//        if (!validarCampos(username)) {
//            return
//        }

        registroViewModel.registrarse(username, email)
    }

    private fun validarCampos(username: String): Boolean{

        val validarUserName = validateUserNameUseCase.validar(username)
        if (validarUserName.isSuccess){
            binding.userNameContainer.isErrorEnabled = false
        } else {
            binding.userNameContainer.error = validarUserName.errorMessage
        }

        return (validarUserName.isSuccess)
    }
}