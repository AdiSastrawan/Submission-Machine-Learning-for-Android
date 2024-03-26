package com.dicoding.asclepius.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.HistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryRowBinding

class HistoryAdapter : ListAdapter<HistoryEntity,HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>(){
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }

        }
    }
    inner class ViewHolder(private val binding : ItemHistoryRowBinding):RecyclerView.ViewHolder(binding.root) {
        fun  bind(history : HistoryEntity){
            binding.tvConfidenceScore.text = history.score
            binding.tvPredictionResult.text = history.result
            binding.ivItem.setImageURI(history.image.toUri())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }
}