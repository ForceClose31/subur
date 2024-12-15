package com.bangkit.subur.features.login.view

import android.content.Intent
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.subur.MainActivity
import com.bangkit.subur.databinding.ActivityLoginBinding
import com.bangkit.subur.features.register.view.RegisterActivity
import com.bangkit.subur.preferences.UserPreferences
import java.util.regex.Pattern

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
        animateLoginPageElements()
        setupValidation()

        binding.loginButton.setOnClickListener {
            if (validateEmail() && validatePassword()) {
                val email = binding.emailInput.text.toString()
                val password = binding.passwordInput.text.toString()

                viewModel.login(
                    email,
                    password,
                    onSuccess = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    onError = { errorMessage ->

                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }


        binding.signupText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupValidation() {
        binding.emailInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateEmail()
        }

        binding.passwordInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validatePassword()
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.emailInput.text.toString().trim()
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
            binding.emailInputLayout.error = "Email cannot be empty"
            false
        } else if (!emailRegex.matcher(email).matches()) {
            binding.emailInputLayout.error = "Invalid email format"
            false
        } else {
            binding.emailInputLayout.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = binding.passwordInput.text.toString()
        return when {
            password.isEmpty() -> {
                binding.passwordInputLayout.error = "Password cannot be empty"
                false
            }
            password.length < 8 -> {
                binding.passwordInputLayout.error = "Password must be at least 8 characters"
                false
            }
            else -> {
                binding.passwordInputLayout.error = null
                true
            }
        }
    }

    private fun animateLoginPageElements() {
        binding.apply {
            logo.animateFadeAndScale(startDelay = 200)
            welcomeText.animateFadeAndScale(startDelay = 400)
            emailInputLayout.animateFadeAndScale(startDelay = 600)
            passwordInputLayout.animateFadeAndScale(startDelay = 800)
            loginButton.animateFadeAndScale(startDelay = 1000)
        }

        binding.signupContainer.translationY = 100f
        ObjectAnimator.ofFloat(binding.signupContainer, "translationY", 0f).apply {
            duration = 600
            interpolator = AccelerateDecelerateInterpolator()
            startDelay = 1200
            start()
        }
    }
    fun View.animateFadeAndScale(
        duration: Long = 500,
        startDelay: Long = 0,
        scaleFrom: Float = 0.7f,
        alphaFrom: Float = 0f
    ) {
        alpha = alphaFrom
        scaleX = scaleFrom
        scaleY = scaleFrom

        ViewCompat.animate(this)
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }
}
