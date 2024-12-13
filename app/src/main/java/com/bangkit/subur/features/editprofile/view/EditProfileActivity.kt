package com.bangkit.subur.features.editprofile.view

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.subur.databinding.ActivityEditProfileBinding
import com.bangkit.subur.preferences.UserPreferences
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

// Define a data class to represent the user profile
data class UserProfile(
    val uid: String,
    val displayName: String,
    val email: String,
    val imageUrl: String?
)

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userPreferences: UserPreferences
    private lateinit var apiService: ApiService
    private var selectedImageUri: Uri? = null
    private var currentProfile: UserProfile? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Glide.with(this).load(it).circleCrop().into(binding.ivProfileImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.101.111.234:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        setupViews()
        fetchUserProfile()
    }

    private fun setupViews() {
        binding.btnSaveProfile.setOnClickListener {
            updateProfile()
        }

        binding.btnChangePhoto.setOnClickListener {
            openImagePicker()
        }
    }

    private fun fetchUserProfile() {
        lifecycleScope.launch {
            try {
                val token = userPreferences.getToken().first()
                val uid = userPreferences.getUid().first()

                if (token == null || uid == null) {
                    Toast.makeText(this@EditProfileActivity, "User data not found", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val response = apiService.getUserProfile("Bearer $token", uid)

                if (response.isSuccessful) {
                    currentProfile = response.body()
                    displayUserProfile()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Failed to fetch profile", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayUserProfile() {
        currentProfile?.let { profile ->
            // Set name in edit text
            binding.etName.setText(profile.displayName)

            // Load profile image if available
            profile.imageUrl?.let { imageUrl ->
                Glide.with(this)
                    .load(imageUrl)
                    .circleCrop()
                    .into(binding.ivProfileImage)
            }

            // You might want to display email or other details if needed
            // binding.tvEmail.text = profile.email
        }
    }

    private fun openImagePicker() {
        getContent.launch("image/*")
    }

    private fun updateProfile() {
        val name = binding.etName.text.toString()

        if (name.isBlank()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val token = userPreferences.getToken().first()
                val uid = userPreferences.getUid().first()

                if (token == null || uid == null) {
                    Toast.makeText(this@EditProfileActivity, "User data not found", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Prepare the request body
                val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())

                // Prepare the image part if an image is selected
                val imagePart = selectedImageUri?.let { uri ->
                    val file = File(getRealPathFromURI(uri))
                    MultipartBody.Part.createFormData(
                        "imageProfile",
                        file.name,
                        file.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                val response = apiService.updateProfile("Bearer $token", uid, namePart, imagePart)

                if (response.isSuccessful) {
                    Toast.makeText(this@EditProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val filePathColumn = arrayOf(android.provider.MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return filePath ?: ""
    }
}

// Update the API service interface
interface ApiService {
    @Multipart
    @PUT("profile/{uid}")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Path("uid") uid: String,
        @Part("displayName") name: RequestBody,
        @Part imageProfile: MultipartBody.Part?
    ): retrofit2.Response<Unit>

    @GET("profile/{uid}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): retrofit2.Response<UserProfile>
}