package com.androiddevs.mvvmnewsapp.ui.uii

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.Repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.ui.viewModel.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.viewModel.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository=NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory =
            NewsViewModelProviderFactory(application,
                newsRepository
            )
         viewModel=ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        //Single-Line for navigation
    bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

    }
}
