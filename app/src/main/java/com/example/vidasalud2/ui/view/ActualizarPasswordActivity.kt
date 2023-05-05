package com.example.vidasalud2.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.vidasalud2.R
import com.example.vidasalud2.data.model.ValidateResultField
import com.example.vidasalud2.databinding.ActivityActualizarPasswordBinding
import com.example.vidasalud2.ui.viewmodel.ActualizarPasswordViewModel
import com.example.vidasalud2.utils.ProgressLoading
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActualizarPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActualizarPasswordBinding

    private val actualizarPasswordViewModel: ActualizarPasswordViewModel by viewModels()

    private val progressLoading = ProgressLoading(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<MaterialToolbar>(R.id.actualizarPasswordToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = "Actualizar Password"
            it.setDisplayHomeAsUpEnabled(true)
        }

        binding.actualizarPasswordBtn.setOnClickListener { actualizarPassword() }

        actualizarPasswordViewModel.msgToastLiveData.observe(this, Observer {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            //actualizarPasswordViewModel.msgToastNull()
        })

        actualizarPasswordViewModel.actualPasswordLiveData.observe(this, Observer {
            observeErrorLiveData(it, binding.actualPasswordContainer)
        })
        actualizarPasswordViewModel.nuevoPasswordLiveData.observe(this, Observer {
            observeErrorLiveData(it, binding.nuevoPasswordContainer)
        })
        actualizarPasswordViewModel.confirmarPasswordLiveData.observe(this, Observer {
            observeErrorLiveData(it, binding.confirmarPasswordContainer)
        })

        actualizarPasswordViewModel.isloadingLiveData.observe(this, Observer {
            progressLoading.mostrarDialog(it)
        })

        actualizarPasswordViewModel.actualizarPasswordIsSuccessLiveData.observe(this) {
            if (it){ navigateConfiguracionCuenta() }
        }
    }

    private fun actualizarPassword() {
        val actualPassword = binding.actualPasswordInput.text.toString().trim()
        val nuevoPassword = binding.nuevoPasswordInput.text.toString().trim()
        val repetirPassword = binding.repetirPasswordInput.text.toString().trim()

        actualizarPasswordViewModel.actualizarPassword(actualPassword, nuevoPassword, repetirPassword)
    }

    //evento volver atras toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun observeErrorLiveData(resultField: ValidateResultField, containerLayout: TextInputLayout){
        if (resultField.isSuccess){
            containerLayout.error = null
        }else {
            containerLayout.error = resultField.errorMessage
        }
    }

    private fun navigateConfiguracionCuenta(){
        val intent = Intent(this, ConfiguracionCuentaActivity::class.java)
        startActivity(intent)
        finish()
    }
}