package com.bangkit.subur.features.profile.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.subur.MainActivity
import com.bangkit.subur.R
import com.bangkit.subur.features.editprofile.view.EditProfileActivity
import com.bangkit.subur.features.profile.data.AppDatabase
import com.bangkit.subur.features.profile.domain.HistoryRepository
import com.bangkit.subur.features.profile.viewmodel.HistoryViewModel
import com.bangkit.subur.features.profile.viewmodel.HistoryViewModelFactory
import com.bangkit.subur.preferences.UserPreferences
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException


class ProfileFragment : Fragment() {

    private lateinit var btnRiwayat: MaterialButton
    private lateinit var contentContainer: FrameLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var userPreferences: UserPreferences
    private lateinit var profileImageView: ImageView
    private lateinit var profileNameTextView: TextView
    private lateinit var profileEmailTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val dao = AppDatabase.getInstance(requireContext()).detectionHistoryDao()
        val repository = HistoryRepository(dao)
        val factory = HistoryViewModelFactory(repository)
        profileImageView = view.findViewById(R.id.profile_image)
        profileNameTextView = view.findViewById(R.id.profile_name)
        profileEmailTextView = view.findViewById(R.id.profile_email)

        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
        userPreferences = UserPreferences(requireContext())

        btnRiwayat = view.findViewById(R.id.btnRiwayat)
        contentContainer = view.findViewById(R.id.contentContainer)
        progressBar = view.findViewById(R.id.progressBar)

        val editProfileButton: MaterialButton = view.findViewById(R.id.editProfileButton)
        editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        btnRiwayat.setOnClickListener { showRiwayatContent() }



        btnRiwayat.setOnClickListener {
            showRiwayatContent()
        }

        val logoutButton: MaterialButton = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }
        loadProfile()

        return view
    }


    private fun showRiwayatContent() {
        progressBar.visibility = View.VISIBLE
        val riwayatContent = layoutInflater.inflate(R.layout.content_riwayat, contentContainer, false)
        contentContainer.removeAllViews()
        contentContainer.addView(riwayatContent)

        btnRiwayat.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        val recyclerView = riwayatContent.findViewById<RecyclerView>(R.id.recyclerViewRiwayat)
        val adapter = HistoryAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        historyViewModel.allHistory.observe(viewLifecycleOwner) { historyList ->
            progressBar.visibility = View.GONE
            adapter.submitList(historyList)
        }
    }

    private fun loadProfile() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val uid = userPreferences.getUid().first()
                val token = userPreferences.getToken().first()

                if (uid.isNullOrEmpty() || token.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        showErrorMessage("User ID or token is missing.")
                    }
                    return@launch
                }

                val profileResponse = fetchProfile(uid, token)
                withContext(Dispatchers.Main) {
                    updateUI(profileResponse)
                }
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Error loading profile", e)
                withContext(Dispatchers.Main) {
                    showErrorMessage("Failed to load profile: ${e.message}")
                }
            }
        }
    }

    private fun fetchProfile(uid: String, token: String): JSONObject {
        val client = OkHttpClient()
        val url = "http://34.101.111.234:3000/profile/$uid"

        try {
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token")
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.e("ProfileFragment", "HTTP error: ${response.code} ${response.message}")
                throw IOException("HTTP error: ${response.code} ${response.message}")
            }

            val responseBody = response.body?.string()
            if (responseBody.isNullOrEmpty()) {
                throw IOException("Response body is empty")
            }

            return JSONObject(responseBody)
        } catch (e: IOException) {
            Log.e("ProfileFragment", "Network error: ${e.message}", e)
            throw IOException("Failed to fetch profile: ${e.message}")
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Unexpected error: ${e.message}", e)
            throw Exception("Unexpected error occurred: ${e.message}")
        }
    }
    private fun updateUI(profileResponse: JSONObject) {
        try {
            val data = profileResponse.getJSONObject("data")
            val name = data.optString("displayName", "N/A")
            val email = data.optString("email", "N/A")
            val imageUrl = data.optString("imageProfile", "")

            profileNameTextView.text = name
            profileEmailTextView.text = email

            if (imageUrl.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(imageUrl)
                    .circleCrop()
                    .into(profileImageView)
            }
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Error updating UI", e)
            showErrorMessage("Failed to update UI: ${e.message}")
        }
    }
    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun logout() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to log out?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            lifecycleScope.launch {
                val userPreferences = UserPreferences(requireContext())
                userPreferences.clear()
                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)

                activity?.finish()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
