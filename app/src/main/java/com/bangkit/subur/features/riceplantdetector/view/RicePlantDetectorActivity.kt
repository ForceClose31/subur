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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.subur.R
import com.bangkit.subur.features.riceplantdetector.viewmodel.RicePlantDetectorViewModel

class RicePlantDetectorActivity : AppCompatActivity() {

    private lateinit var btnTakePhoto: Button
    private lateinit var btnSelectFromGallery: Button
    private lateinit var imageView: ImageView
    private lateinit var textViewMessage: TextView
    private lateinit var ricePlantDetectorViewModel: RicePlantDetectorViewModel

    private val takePhotoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            updateUI(imageBitmap)
            ricePlantDetectorViewModel.uploadImage(imageBitmap)
        }
    }

    private val selectFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            updateUI(bitmap)
            ricePlantDetectorViewModel.uploadImage(bitmap)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rice_plant_detector)

        btnTakePhoto = findViewById(R.id.btnTakePhoto)
        btnSelectFromGallery = findViewById(R.id.btnSelectFromGallery)
        imageView = findViewById(R.id.imageView)
        textViewMessage = findViewById(R.id.textViewMessage)

        ricePlantDetectorViewModel = ViewModelProvider(this).get(RicePlantDetectorViewModel::class.java)

        btnTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoResult.launch(intent)
        }

        btnSelectFromGallery.setOnClickListener {
            selectFromGalleryResult.launch("image/*")
        }

        ricePlantDetectorViewModel.result.observe(this) { result ->
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
