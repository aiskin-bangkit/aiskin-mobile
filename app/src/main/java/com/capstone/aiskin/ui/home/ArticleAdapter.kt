package com.capstone.aiskin.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.aiskin.databinding.ItemArticleBinding

data class ArticleItem(
    val imageUrl: String,
    val title: String,
    val description: String,
    val createdAt: String
)

class ArticleAdapter(private val articleList: List<ArticleItem>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleItem) {
            with(binding) {
                tvItemName.text = article.title
                tvItemDescription.text = article.description
                tvItemCreatedAt.text = article.createdAt

                Glide.with(imgItemPhoto.context)
                    .load("https://www.wowkeren.com/display/images/photo/2023/04/10/00476771.jpg")
                    .into(imgItemPhoto)
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
