package com.capstone.aiskin.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.aiskin.core.data.network.article.response.ArticleResponseItem
import com.capstone.aiskin.databinding.ItemArticleBinding
import com.capstone.aiskin.core.helper.DateTimeConverter

class ArticleAdapter(
    private val articleList: List<ArticleResponseItem>,
    private val onItemClick: (String) -> Unit
    ) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleResponseItem) {
            with(binding) {
                val formattedDate = article.createdAt?.let {
                    DateTimeConverter.formatTimestamp(it)
                } ?: "Unknown Date"

                tvItemName.text = article.name ?: "Unknown"
                tvItemDescription.text = article.description ?: "No description available"
                tvItemCreatedAt.text = formattedDate

                Glide.with(imgItemPhoto.context)
                    .load(article.image ?:"https://via.placeholder.com/150")
                    .into(imgItemPhoto)

                root.setOnClickListener {
                    article.id?.let { id -> onItemClick(id) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articleList[position])
    }

    override fun getItemCount(): Int = articleList.size
}
