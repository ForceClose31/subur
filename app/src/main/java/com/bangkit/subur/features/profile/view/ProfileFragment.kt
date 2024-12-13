package com.bangkit.subur.features.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.subur.R
import com.bangkit.subur.features.profile.data.AppDatabase
import com.bangkit.subur.features.profile.data.DetectionHistory
import com.bangkit.subur.features.profile.domain.HistoryRepository
import com.bangkit.subur.features.profile.viewmodel.HistoryViewModel
import com.bangkit.subur.features.profile.viewmodel.HistoryViewModelFactory
import com.google.android.material.button.MaterialButton

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

        btnUnggahan.setOnClickListener { showUnggahanContent() }
        btnRiwayat.setOnClickListener { showRiwayatContent() }

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

        val recyclerView = riwayatContent.findViewById<RecyclerView>(R.id.recyclerViewRiwayat)
        val adapter = HistoryAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        historyViewModel.allHistory.observe(viewLifecycleOwner) { historyList ->
            adapter.submitList(historyList)
        }
    }
}
