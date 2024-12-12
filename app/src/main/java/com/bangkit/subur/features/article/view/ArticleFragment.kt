package com.bangkit.subur.features.article.view

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.subur.R
import com.bangkit.subur.databinding.FragmentArticleBinding
import com.bangkit.subur.features.article.viewmodel.ArticleViewModel
import com.bangkit.subur.features.article.model.Article

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: ArticleViewModel

    private lateinit var horizontalAdapter: HorizontalArticleAdapter
    private lateinit var verticalAdapter: ArticleAdapter
    private lateinit var latestAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[ArticleViewModel::class.java]

        setupRecyclerViews()
        setupObservers()
        setupTabSwitching()
        setupSearch()

        viewModel.fetchArticles()

        return binding.root
    }

    private fun setupRecyclerViews() {
        horizontalAdapter = HorizontalArticleAdapter { openArticle(it) }
        verticalAdapter = ArticleAdapter { openArticle(it) }
        latestAdapter = ArticleAdapter { openArticle(it) }

        binding.rvHorizontalArticles.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = horizontalAdapter
        }

        binding.rvVerticalArticles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = verticalAdapter
        }

        binding.rvLatestArticles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = latestAdapter
        }
    }

    private fun setupObservers() {
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            if (articles.isNotEmpty()) {
                Log.d("ArticleFragment", "Articles loaded: $articles")
                horizontalAdapter.submitList(articles.take(5))
                verticalAdapter.submitList(articles)
                latestAdapter.submitList(articles.take(5))
            } else {
                Log.d("ArticleFragment", "No articles available")
            }
        }
    }

    private fun setupTabSwitching() {
        binding.tvTerbaru.setOnClickListener {
            switchToTab(isTerbaru = true)
        }

        binding.tvBeranda.setOnClickListener {
            switchToTab(isTerbaru = false)
        }
    }

    private fun switchToTab(isTerbaru: Boolean) {
        if (isTerbaru) {
            binding.tvTerbaru.setTextColor(Color.parseColor("#388E3C"))
            binding.tvBeranda.setTextColor(Color.parseColor("#BDBDBD"))

            binding.viewSwitcher.setInAnimation(context, R.anim.slide_in_right)
            binding.viewSwitcher.setOutAnimation(context, R.anim.slide_out_left)

            viewModel.articles.value?.let { articles ->
                latestAdapter.submitList(articles.take(5))
            }
        } else {
            binding.tvBeranda.setTextColor(Color.parseColor("#388E3C"))
            binding.tvTerbaru.setTextColor(Color.parseColor("#BDBDBD"))

            binding.viewSwitcher.setInAnimation(context, R.anim.slide_in_left)
            binding.viewSwitcher.setOutAnimation(context, R.anim.slide_out_right)

            viewModel.articles.value?.let { articles ->
                verticalAdapter.submitList(articles)
            }
        }
        binding.viewSwitcher.showNext()
    }

    private fun openArticle(article: Article) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.article_link))
        startActivity(intent)
    }

    private fun setupSearch() {
        binding.etSearchArticle.addTextChangedListener { editable ->
            val query = editable.toString().trim()
            viewModel.searchArticles(query)
        }
    }
}
