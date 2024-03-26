package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsRowBinding

class NewsAdapter : ListAdapter<ArticlesItem,NewsAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

        }
    }
    inner class ViewHolder(private val binding : ItemNewsRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(articlesItem: ArticlesItem){
            Log.d("NewsAdapter",articlesItem.toString())
            binding.tvTitle.text = articlesItem.title
            binding.tvDescription.text = articlesItem.description
            if(articlesItem.urlToImage != null){
                Glide.with(itemView)
                    .load(articlesItem.urlToImage)
                    .into(binding.ivNews)
            }else{
                binding.ivNews.setImageResource(R.drawable.ic_place_holder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
        holder.itemView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
            holder.itemView.context.startActivity(intent)
        }
    }

}