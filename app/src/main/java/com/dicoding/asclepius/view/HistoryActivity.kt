package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.helper.ViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var viewModel: HistoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "History"

        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this,factory)[HistoryViewModel::class.java]
        viewModel.getHistory().observe(this){
            adapter.submitList(it)
        }
        adapter = HistoryAdapter()
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        val itemDecorations = DividerItemDecoration(this, LinearLayoutManager(this).orientation)
        binding.rvHistory.addItemDecoration(itemDecorations)
        binding.rvHistory.setHasFixedSize(true)
        binding.rvHistory.adapter = adapter
    }


}