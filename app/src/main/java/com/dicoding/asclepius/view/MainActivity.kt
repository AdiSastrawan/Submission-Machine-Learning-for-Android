package com.dicoding.asclepius.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.helper.ViewModelFactory
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private var imageClassifierHelper : ImageClassifierHelper? = null
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Home"
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this,factory)[MainViewModel::class.java]
        binding.galleryButton.setOnClickListener{
            startGallery()
        }
        binding.historyButton.setOnClickListener{
            moveToHistory()
        }
        binding.analyzeButton.setOnClickListener{
            currentImageUri?.let {
                analyzeImage()
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }

        }
        binding.newsButton.setOnClickListener{
            moveToNews()
        }
    }

    private fun moveToHistory() {
        val intent = Intent(this,HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun moveToNews(){
        val intent = Intent(this,NewsActivity::class.java)
        startActivity(intent)
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
                                val sortedCategories = it[0].categories.sortedByDescending { it.score }
                                binding.progressIndicator.visibility = View.GONE
                                val displayResult =
                                    sortedCategories.joinToString("\n") {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()
                                    }
                                val history = HistoryEntity(
                                    image = currentImageUri.toString(),
                                    result = sortedCategories[0].label.toString(),
                                    score = NumberFormat.getPercentInstance()
                                        .format(sortedCategories[0].score).trim()
                                )
                                viewModel.insert(history)
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
    ){
        uri ->
        if(uri != null){

            UCrop.of(uri, uri)
                .withMaxResultSize(2000, 2000)
                .start(this)
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
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.e("CropError",cropError.toString())
            showToast(cropError?.message.toString())
        }
    }
}