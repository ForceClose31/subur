package com.bangkit.subur.features.login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.MainActivity
import com.bangkit.subur.databinding.ActivityLoginBinding
import com.bangkit.subur.features.login.data.LoginApiService
import com.bangkit.subur.features.login.data.LoginLocalDataSource
import com.bangkit.subur.features.login.domain.LoginResult
import com.bangkit.subur.features.login.domain.LoginRepositoryImpl
import com.bangkit.subur.features.login.domain.LoginUseCase
import com.bangkit.subur.features.register.view.RegisterActivity
import com.bangkit.subur.preferences.UserPreferences
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreferences = UserPreferences(this)
        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(userPreferences)
        )[LoginViewModel::class.java]

        // Login button click listener
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(
                    email,
                    password,
                    onSuccess = {
                        // Navigate to main activity or dashboard
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    onError = { errorMessage ->
                        // Show error message
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Sign Up text click listener
        binding.signupText.setOnClickListener {
            // Navigate to RegisterActivity
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}