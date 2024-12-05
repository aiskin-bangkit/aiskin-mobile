package com.capstone.aiskin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.aiskin.core.data.network.article.response.ArticleResponseItem
import com.capstone.aiskin.databinding.ItemHeroNewsBinding

class HeroArticleAdapter(
    private val articleList: List<ArticleResponseItem>,
    private val onItemClick: (String) -> Unit
    ) :
    RecyclerView.Adapter<HeroArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ItemHeroNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleResponseItem) {
            with(binding) {
                tvTitle.text = article.name ?: "Unknown"
                tvSubtitle.text = article.description ?: "No description available"

                Glide.with(imgBackground.context)
                    .load(article.image ?:"https://via.placeholder.com/150")
                    .into(imgBackground)

                root.setOnClickListener {
                    article.id?.let { id -> onItemClick(id) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemHeroNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articleList[position])
    }

    override fun getItemCount(): Int = articleList.size
}
