package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.helper.ViewModelFactory

class NewsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "News"
        val factory = ViewModelFactory.getInstance(this.application)
        val viewModel = ViewModelProvider(this,factory)[NewsViewModel::class.java]

        val adapter = NewsAdapter()
        viewModel.articlesItem.observe(this){
            adapter.submitList(it)
        }
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        val itemDecorations = DividerItemDecoration(this, LinearLayoutManager(this).orientation)
        binding.rvNews.addItemDecoration(itemDecorations)
        binding.rvNews.adapter = adapter

        viewModel.isLoading.observe(this){
            binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
        }
    }
}