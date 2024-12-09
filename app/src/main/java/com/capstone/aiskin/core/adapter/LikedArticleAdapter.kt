package com.capstone.aiskin.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.aiskin.core.data.local.article.entity.ArticleEntity
import com.capstone.aiskin.databinding.ItemArticleBinding
import com.capstone.aiskin.core.helper.DateTimeConverter

class LikedArticleAdapter(
    private val articleList: List<ArticleEntity>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<LikedArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleEntity) {
            with(binding) {
                val formattedDate = article.createdAt.let {
                    DateTimeConverter.formatTimestamp(it)
                }

                tvItemName.text = article.name
                tvItemDescription.text = article.content
                tvItemCreatedAt.text = formattedDate

                Glide.with(imgItemPhoto.context)
                    .load(article.imageUrl)
                    .into(imgItemPhoto)

                root.setOnClickListener {
                    article.id.let { id -> onItemClick(id) }
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