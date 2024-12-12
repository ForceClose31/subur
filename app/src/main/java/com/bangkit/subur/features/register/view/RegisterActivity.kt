package com.bangkit.subur.features.register.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.regex.Pattern
import com.bangkit.subur.R
import com.bangkit.subur.features.login.view.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var nameInput: TextInputEditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInput: TextInputEditText
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var registerButton: MaterialButton

    private val client = OkHttpClient()
    private val baseUrl = "http://34.101.111.234:3000/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize views
        nameInput = findViewById(R.id.name_input)
        nameInputLayout = findViewById(R.id.name_input_layout)
        emailInput = findViewById(R.id.email_input)
        emailInputLayout = findViewById(R.id.email_input_layout)
        passwordInput = findViewById(R.id.password_input)
        passwordInputLayout = findViewById(R.id.password_input_layout)
        registerButton = findViewById(R.id.register_button)

        // Add text change listeners for real-time validation
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
                registerUser(name, email, password)
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

    private fun registerUser(name: String, email: String, password: String) {
        val jsonObject = JSONObject().apply {
            put("email", email)
            put("password", password)
            put("displayName", name)
        }

        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("${baseUrl}register")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Registration failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val responseBody = response.body?.string()
                    runOnUiThread {
                        try {
                            if (response.isSuccessful) {
                                Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                                // Navigate to LoginActivity
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish() // Close the RegisterActivity
                            } else {
                                // Parse the error response
                                val errorJson = JSONObject(responseBody ?: "{}")
                                val errorMessage = when {
                                    errorJson.has("message") -> errorJson.getString("message")
                                    errorJson.has("error") -> {
                                        val errorObj = errorJson.getJSONObject("error")
                                        when {
                                            errorObj.has("details") -> errorObj.getString("details")
                                            errorObj.has("message") -> errorObj.getString("message")
                                            else -> "Registration failed"
                                        }
                                    }
                                    else -> "Registration failed: ${response.message}"
                                }

                                // Display the specific error message
                                Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_LONG).show()

                                // Optional: Handle specific error scenarios
                                if (errorMessage.contains("email address is already in use", ignoreCase = true)) {
                                    emailInputLayout.error = "Email is already registered"
                                    emailInput.requestFocus()
                                }
                            }
                        } catch (e: Exception) {
                            // Fallback error handling
                            Toast.makeText(this@RegisterActivity, "Error processing registration response", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }
}