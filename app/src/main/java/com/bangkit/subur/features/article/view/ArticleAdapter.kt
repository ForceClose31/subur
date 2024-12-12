package com.bangkit.subur.features.article.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.subur.databinding.ItemArticleBinding
import com.bangkit.subur.features.article.model.Article

class ArticleAdapter(private val onItemClick: (Article) -> Unit) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private var articles: List<Article> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Article>) {
        articles = list
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.short_description
            binding.root.setOnClickListener { onItemClick(article) }
        }
    }
}
