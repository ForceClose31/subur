package com.bangkit.subur.features.riceplantdetector.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.bangkit.subur.R
import com.bangkit.subur.features.profile.data.AppDatabase
import com.bangkit.subur.features.profile.data.DetectionHistory
import com.bangkit.subur.features.profile.viewmodel.HistoryViewModel
import com.bangkit.subur.features.profile.viewmodel.HistoryViewModelFactory
import com.bangkit.subur.features.riceplantdetector.viewmodel.RicePlantDetectorViewModel
import androidx.fragment.app.viewModels
import com.bangkit.subur.features.profile.domain.HistoryRepository

class RicePlantDetectorFragment : Fragment(R.layout.fragment_rice_plant_detector) {

    private lateinit var btnTakePhoto: Button
    private lateinit var btnSelectFromGallery: Button
    private lateinit var imageView: ImageView
    private lateinit var textViewMessage: TextView
    private lateinit var historyViewModel: HistoryViewModel

    private val ricePlantDetectorViewModel: RicePlantDetectorViewModel by viewModels()

    private val takePhotoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            updateUI(imageBitmap)
            ricePlantDetectorViewModel.uploadImage(imageBitmap)
        }
    }

    private val selectFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
            updateUI(bitmap)
            ricePlantDetectorViewModel.uploadImage(bitmap)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getInstance(requireContext())
        val repository = HistoryRepository(database.detectionHistoryDao())

        val factory = HistoryViewModelFactory(repository)
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        btnTakePhoto = view.findViewById(R.id.btnTakePhoto)
        btnSelectFromGallery = view.findViewById(R.id.btnSelectFromGallery)
        imageView = view.findViewById(R.id.imageView)
        textViewMessage = view.findViewById(R.id.textViewMessage)

        btnTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoResult.launch(intent)
        }

        btnSelectFromGallery.setOnClickListener {
            selectFromGalleryResult.launch("image/*")
        }

        ricePlantDetectorViewModel.result.observe(viewLifecycleOwner) { result ->
            textViewMessage.text = "Predicted Class: ${result.predicted_class}\nConfidence: ${result.confidence}\nHandling Instruction: ${result.handling_instructions}"
            val historyItem = DetectionHistory(
                imageUri = saveImageToInternalStorage(),
                confidence = result.confidence,
                handlingInstructions = result.handling_instructions,
                predictedClass = result.predicted_class
            )
            historyViewModel.insertHistory(historyItem)
        }
    }

    private fun saveImageToInternalStorage(): String {
        val filename = "rice_plant_${System.currentTimeMillis()}.png"
        val outputStream = requireContext().openFileOutput(filename, Activity.MODE_PRIVATE)
        imageView.drawable.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        return requireContext().filesDir.absolutePath + "/" + filename
    }

    private fun updateUI(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)

        btnTakePhoto.visibility = View.GONE
        btnSelectFromGallery.visibility = View.GONE

        imageView.visibility = View.VISIBLE
        textViewMessage.visibility = View.VISIBLE
    }
}
