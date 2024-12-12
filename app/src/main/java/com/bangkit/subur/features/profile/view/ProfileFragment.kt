package com.bangkit.subur.features.profile.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.subur.R
import com.bangkit.subur.features.editprofile.view.EditProfileActivity
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Find the button by its ID
        val editProfileButton: MaterialButton = view.findViewById(R.id.editProfileButton)
        editProfileButton.setOnClickListener {
            // Create an intent to start EditProfileActivity
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
