package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private var imageClassifierHelper : ImageClassifierHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.galleryButton.setOnClickListener{
            startGallery()
        }
        binding.analyzeButton.setOnClickListener{
            currentImageUri?.let {
                analyzeImage()
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }

        }
    }

    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(currentImageUri)
        }
    }

    private fun analyzeImage() {
        binding.progressIndicator.visibility = View.VISIBLE
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener{
                override fun onError(e: String) {
                    showToast(e)
                }

                override fun onResult(result: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        result?.let {it ->
                            if(it.isNotEmpty() && it[0].categories.isNotEmpty() ){
                                println(it)
//                                Log.d("Cancer",it.toString())
                                val sortedCategories = it[0].categories.sortedByDescending { it.score }
                                binding.progressIndicator.visibility = View.GONE
                                val displayResult =
                                    sortedCategories.joinToString("\n") {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()
                                    }
                                moveToResult(displayResult)
                            }
                        }

                    }
                }

            }
        )
        imageClassifierHelper?.classifyStaticImage(currentImageUri!!)

    }

    private fun moveToResult(result : String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE,currentImageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_RESULT,result)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){ uri ->
        if(uri != null){
            currentImageUri = uri
            showImage()
        }else{
            Log.d("Photo Picker", "No media selected")
        }

    }
}