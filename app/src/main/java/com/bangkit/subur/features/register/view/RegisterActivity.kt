package com.bangkit.subur.features.register.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.subur.R
import com.bangkit.subur.features.login.view.LoginActivity
import com.bangkit.subur.features.register.domain.model.RegisterRequest
import com.bangkit.subur.features.register.domain.usecase.RegisterUseCase
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var nameInput: TextInputEditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInput: TextInputEditText
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var registerButton: MaterialButton

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        nameInput = findViewById(R.id.name_input)
        nameInputLayout = findViewById(R.id.name_input_layout)
        emailInput = findViewById(R.id.email_input)
        emailInputLayout = findViewById(R.id.email_input_layout)
        passwordInput = findViewById(R.id.password_input)
        passwordInputLayout = findViewById(R.id.password_input_layout)
        registerButton = findViewById(R.id.register_button)

        val loginText: TextView = findViewById(R.id.login_text)
        loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setupValidation()
        observeRegistrationResult()
    }


    private fun setupValidation() {
        nameInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateName()
        }
        emailInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateEmail()
        }
        passwordInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validatePassword()
        }

        registerButton.setOnClickListener {
            if (validateName() && validateEmail() && validatePassword()) {
                val name = nameInput.text.toString()
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                registerViewModel.register(RegisterRequest(email, password, name))
            }
        }
    }

    private fun observeRegistrationResult() {
        registerViewModel.registrationResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }.onFailure { error ->
                val errorMessage = when (error) {
                    is RegisterUseCase.RegistrationException -> error.message
                    else -> "Registration failed"
                }

                when {
                    errorMessage?.contains("email address is already in use", ignoreCase = true) == true -> {
                        emailInputLayout.error = "Email is already registered"
                        emailInput.requestFocus()
                    }
                    else -> Toast.makeText(this, errorMessage ?: "Registration failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validateName(): Boolean {
        val name = nameInput.text.toString().trim()
        return if (name.isEmpty()) {
            nameInputLayout.error = "Name cannot be empty"
            false
        } else if (name.length < 2) {
            nameInputLayout.error = "Name must be at least 2 characters"
            false
        } else {
            nameInputLayout.error = null
            true
        }
    }

    private fun validateEmail(): Boolean {
        val email = emailInput.text.toString().trim()
        val emailRegex = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return if (email.isEmpty()) {
            emailInputLayout.error = "Email cannot be empty"
            false
        } else if (!emailRegex.matcher(email).matches()) {
            emailInputLayout.error = "Invalid email format"
            false
        } else {
            emailInputLayout.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = passwordInput.text.toString()
        return when {
            password.isEmpty() -> {
                passwordInputLayout.error = "Password cannot be empty"
                false
            }
            password.length < 8 -> {
                passwordInputLayout.error = "Password must be at least 8 characters"
                false
            }
            !password.matches(".*[A-Z].*".toRegex()) -> {
                passwordInputLayout.error = "Password must contain an uppercase letter"
                false
            }
            !password.matches(".*[a-z].*".toRegex()) -> {
                passwordInputLayout.error = "Password must contain a lowercase letter"
                false
            }
            !password.matches(".*\\d.*".toRegex()) -> {
                passwordInputLayout.error = "Password must contain a number"
                false
            }
            else -> {
                passwordInputLayout.error = null
                true
            }
        }
    }

}