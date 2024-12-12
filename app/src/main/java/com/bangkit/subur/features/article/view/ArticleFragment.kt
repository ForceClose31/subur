package com.bangkit.subur.features.article.view

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.subur.R
import com.bangkit.subur.features.article.viewmodel.ArticleViewModel
import com.bangkit.subur.features.article.model.Article

class ArticleFragment : Fragment() {

    private lateinit var tvTerbaru: TextView
    private lateinit var tvBeranda: TextView
    private lateinit var viewSwitcher: ViewSwitcher
    private lateinit var viewModel: ArticleViewModel

    private lateinit var horizontalAdapter: HorizontalArticleAdapter
    private lateinit var verticalAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: View = inflater.inflate(R.layout.fragment_article, container, false)

        tvTerbaru = binding.findViewById(R.id.tvTerbaru)
        tvBeranda = binding.findViewById(R.id.tvBeranda)
        viewSwitcher = binding.findViewById(R.id.viewSwitcher)

        viewModel = ViewModelProvider(this)[ArticleViewModel::class.java]

        horizontalAdapter = HorizontalArticleAdapter { openArticle(it) }
        verticalAdapter = ArticleAdapter { openArticle(it) }

        viewModel.articles.observe(viewLifecycleOwner, { articles ->
            horizontalAdapter.submitList(articles.take(5))
            verticalAdapter.submitList(articles)
        })

        tvTerbaru.setOnClickListener {
            tvTerbaru.setTextColor(Color.parseColor("#388E3C"))
            tvBeranda.setTextColor(Color.parseColor("#BDBDBD"))

            viewSwitcher.setInAnimation(context, R.anim.slide_in_right)
            viewSwitcher.setOutAnimation(context, R.anim.slide_out_left)

            viewSwitcher.showNext()
        }

        tvBeranda.setOnClickListener {
            tvBeranda.setTextColor(Color.parseColor("#388E3C"))
            tvTerbaru.setTextColor(Color.parseColor("#BDBDBD"))

            viewSwitcher.setInAnimation(context, R.anim.slide_in_left)
            viewSwitcher.setOutAnimation(context, R.anim.slide_out_right)

            viewSwitcher.showNext()
        }

        viewModel.fetchArticles()

        return binding
    }

    private fun openArticle(article: Article) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.article_link))
        startActivity(intent)
    }
}
