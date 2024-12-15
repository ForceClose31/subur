package com.bangkit.subur.features.editprofile.view

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.subur.features.editprofile.domain.EditProfileUseCase
import com.bangkit.subur.features.editprofile.domain.UserProfile
import kotlinx.coroutines.launch
import java.io.File

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val editProfileUseCase = EditProfileUseCase(application)
    private var currentProfile: UserProfile? = null

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: MutableLiveData<UserProfile?> = _userProfile

    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> = _selectedImageUri

    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                // Only fetch if we don't already have a profile
                if (currentProfile == null) {
                    val profile = editProfileUseCase.getUserProfile()
                    currentProfile = profile
                    _userProfile.value = profile
                } else {
                    // If we already have a profile, just post the existing value
                    _userProfile.value = currentProfile
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            }
        }
    }

    fun updateProfile(name: String, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                val file = imageUri?.let { File(getRealPathFromURI(it)) }
                val success = editProfileUseCase.updateProfile(name, file)
                _updateResult.value = success
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val contentResolver = getApplication<Application>().contentResolver
        val filePathColumn = arrayOf(android.provider.MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return filePath ?: ""
    }
}