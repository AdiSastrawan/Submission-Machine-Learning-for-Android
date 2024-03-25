package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.util.FileUtils
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private var imageClassifierHelper : ImageClassifierHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(checkStoragePermissions()){
            showToast("Permission Granted")
        }else{
            showToast("Permission Denied")
        }
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
    fun checkStoragePermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            Environment.isExternalStorageManager()
        } else {
            //Below android 11
            val write =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
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
            val storageDir = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ), "Test"
            )
            val destinationUri = File(storageDir,"${System.currentTimeMillis()}.jpg").toUri()
            Log.d("destinationUri",destinationUri.toString())
            UCrop.of(uri, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(2000, 2000)
                .start(this);
            showImage()
        }else{
            Log.d("Photo Picker", "No media selected")
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            showToast(resultUri.toString())
            currentImageUri = resultUri
            Log.d("ImageUri",currentImageUri.toString())
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.e("CropError",cropError.toString())
            showToast(cropError?.message.toString())
        }
    }
}