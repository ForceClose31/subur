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
import com.bangkit.subur.R
import com.bangkit.subur.features.riceplantdetector.viewmodel.RicePlantDetectorViewModel
import androidx.fragment.app.viewModels

class RicePlantDetectorFragment : Fragment(R.layout.fragment_rice_plant_detector) {

    private lateinit var btnTakePhoto: Button
    private lateinit var btnSelectFromGallery: Button
    private lateinit var imageView: ImageView
    private lateinit var textViewMessage: TextView
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
        }
    }

    private fun updateUI(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)

        btnTakePhoto.visibility = View.GONE
        btnSelectFromGallery.visibility = View.GONE

        imageView.visibility = View.VISIBLE
        textViewMessage.visibility = View.VISIBLE
    }
}
