package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
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
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this,factory).get(HistoryViewModel::class.java)
        viewModel.getHistory().observe(this){it->
            adapter.submitList(it)
        }
        adapter = HistoryAdapter()
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.setHasFixedSize(true)
        binding.rvHistory.adapter = adapter
    }


}