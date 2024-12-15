package com.bangkit.subur.features.editprofile.view

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.subur.databinding.ActivityEditProfileBinding
import com.bumptech.glide.Glide

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.setSelectedImageUri(it)
            Glide.with(this).load(it).circleCrop().into(binding.ivProfileImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]

        setupViews()
        observeViewModel()
        viewModel.fetchUserProfile()
    }

    private fun setupViews() {
        binding.btnSaveProfile.setOnClickListener {
            updateProfile()
        }

        binding.btnChangePhoto.setOnClickListener {
            openImagePicker()
        }
    }

    private fun observeViewModel() {
        viewModel.userProfile.observe(this) { profile ->
            // Only update UI if profile is not null
            profile?.let {
                binding.etName.setText(it.displayName)
                it.imageUrl?.let { imageUrl ->
                    Glide.with(this)
                        .load(imageUrl)
                        .circleCrop()
                        .into(binding.ivProfileImage)
                }
            }
        }

        viewModel.selectedImageUri.observe(this) { uri ->
            uri?.let {
                Glide.with(this).load(it).circleCrop().into(binding.ivProfileImage)
            }
        }

        viewModel.updateResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
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

        viewModel.updateProfile(name, viewModel.selectedImageUri.value)
    }
}