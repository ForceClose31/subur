package com.bangkit.subur.features.login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

import com.bangkit.subur.preferences.UserPreferences
import com.bangkit.subur.features.login.data.LoginState
import com.bangkit.subur.features.register.view.RegisterActivity
import com.bangkit.subur.MainActivity
import com.bangkit.subur.databinding.ActivityLoginBinding
import com.bangkit.subur.features.emaillogin.EmailLinkAuthActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        val userPreferences = UserPreferences(this)
        loginViewModel = LoginViewModel(auth, userPreferences)

        setupInputValidation()
        setupLoginButton()
        observeLoginState()
        setupSignupNavigation()
        setupEmailLoginNavigation()
        setupForgotPassword()
    }

    private fun setupInputValidation() {
        // Real-time validation on focus change
        binding.emailInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateEmail()
        }

        binding.passwordInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validatePassword()
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.emailInput.text.toString().trim()
        return when {
            email.isEmpty() -> {
                binding.emailInputLayout.error = "Email cannot be empty"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailInputLayout.error = "Invalid email format"
                false
            }
            else -> {
                binding.emailInputLayout.error = null
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val password = binding.passwordInput.text.toString().trim()
        return when {
            password.isEmpty() -> {
                binding.passwordInputLayout.error = "Password cannot be empty"
                false
            }
            password.length < 6 -> {
                binding.passwordInputLayout.error = "Password must be at least 6 characters"
                false
            }
            else -> {
                binding.passwordInputLayout.error = null
                true
            }
        }
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            // Perform full validation before login
            if (validateEmail() && validatePassword()) {
                loginViewModel.login(email, password)
            }
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            loginViewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.Loading -> {
                        binding.loginButton.isEnabled = false
                        // Show loading indicator
                    }
                    is LoginState.Success -> {
                        // Navigate to main activity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is LoginState.Error -> {
                        binding.loginButton.isEnabled = true
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is LoginState.Idle -> {
                        binding.loginButton.isEnabled = true
                    }
                }
            }
        }
    }

    private fun setupSignupNavigation() {
        binding.signupText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupEmailLoginNavigation() {
        binding.emailLinkLoginButton.setOnClickListener {
            val intent = Intent(this, EmailLinkAuthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupForgotPassword() {
        //  binding.forgotPassword.setOnClickListener {
        //      val intent = Intent(this, ForgotPasswordActivity::class.java)
        //      startActivity(intent)
        //  }


    }
}