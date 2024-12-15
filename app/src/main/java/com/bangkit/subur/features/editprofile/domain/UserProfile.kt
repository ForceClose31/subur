package com.bangkit.subur.features.editprofile.domain

data class UserProfile(
    val uid: String,
    val displayName: String,
    val email: String,
    val imageUrl: String?
)