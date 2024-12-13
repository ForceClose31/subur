package com.bangkit.subur.features.profile.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.bangkit.subur.R
import com.bangkit.subur.features.editprofile.view.EditProfileActivity
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() {

    private lateinit var btnUnggahan: MaterialButton
    private lateinit var btnRiwayat: MaterialButton
    private lateinit var contentContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        btnUnggahan = view.findViewById(R.id.btnUnggahan)
        btnRiwayat = view.findViewById(R.id.btnRiwayat)
        contentContainer = view.findViewById(R.id.contentContainer)

        val editProfileButton: MaterialButton = view.findViewById(R.id.editProfileButton)
        editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        btnUnggahan.setOnClickListener {
            showUnggahanContent()
        }

        btnRiwayat.setOnClickListener {
            showRiwayatContent()
        }

        // Show Unggahan content by default
        showUnggahanContent()

        return view
    }

    private fun showUnggahanContent() {
        val unggahanContent = layoutInflater.inflate(R.layout.content_unggahan, contentContainer, false)
        contentContainer.removeAllViews()
        contentContainer.addView(unggahanContent)

        btnUnggahan.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        btnRiwayat.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
    }

    private fun showRiwayatContent() {
        val riwayatContent = layoutInflater.inflate(R.layout.content_riwayat, contentContainer, false)
        contentContainer.removeAllViews()
        contentContainer.addView(riwayatContent)

        btnUnggahan.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
        btnRiwayat.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }
}
