package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE))
        val result = intent.getStringExtra(EXTRA_RESULT)
        binding.resultImage.setImageURI(imageUri)
        binding.resultText.text = result

    }

    companion object{
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_RESULT = "extra_result"
    }

}