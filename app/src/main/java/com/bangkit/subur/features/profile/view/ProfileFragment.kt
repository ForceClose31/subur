package com.bangkit.subur.features.profile.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bangkit.subur.MainActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.subur.R
import com.bangkit.subur.features.editprofile.view.EditProfileActivity
import com.bangkit.subur.preferences.UserPreferences
import com.bangkit.subur.features.profile.data.AppDatabase
import com.bangkit.subur.features.profile.domain.HistoryRepository
import com.bangkit.subur.features.profile.viewmodel.HistoryViewModel
import com.bangkit.subur.features.profile.viewmodel.HistoryViewModelFactory
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var btnUnggahan: MaterialButton
    private lateinit var btnRiwayat: MaterialButton
    private lateinit var contentContainer: FrameLayout
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val dao = AppDatabase.getInstance(requireContext()).detectionHistoryDao()
        val repository = HistoryRepository(dao)
        val factory = HistoryViewModelFactory(repository)
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        btnUnggahan = view.findViewById(R.id.btnUnggahan)
        btnRiwayat = view.findViewById(R.id.btnRiwayat)
        contentContainer = view.findViewById(R.id.contentContainer)

        val editProfileButton: MaterialButton = view.findViewById(R.id.editProfileButton)
        editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        
        btnUnggahan.setOnClickListener { showUnggahanContent() }
        btnRiwayat.setOnClickListener { showRiwayatContent() }


        btnUnggahan.setOnClickListener {
            showUnggahanContent()
        }

        btnRiwayat.setOnClickListener {
            showRiwayatContent()
        }

        // Show Unggahan content by default
        showUnggahanContent()

        val logoutButton: MaterialButton = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }

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

        val recyclerView = riwayatContent.findViewById<RecyclerView>(R.id.recyclerViewRiwayat)
        val adapter = HistoryAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        historyViewModel.allHistory.observe(viewLifecycleOwner) { historyList ->
            adapter.submitList(historyList)
        }
    }

    private fun logout() {
        // Create a confirmation dialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to log out?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            lifecycleScope.launch {
                // Clear user preferences
                val userPreferences = UserPreferences(requireContext())
                userPreferences.clear()

                // Redirect to MainActivity
                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    // Clear the back stack and create a new task
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)

                // Finish the current fragment/activity to prevent going back
                activity?.finish()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        // Make sure to import AlertDialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
