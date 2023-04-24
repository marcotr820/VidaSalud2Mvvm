package com.example.vidasalud2.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.vidasalud2.R
import com.example.vidasalud2.databinding.ActivityHomeBinding
import com.example.vidasalud2.databinding.ActivityMainBinding
import com.example.vidasalud2.ui.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    //datastore
    private val dataStoreViewModel: DataViewModel by viewModels()

    //viewBinding
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetToken.setOnClickListener {
            showToast(dataStoreViewModel.getToken().toString())
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}