package com.androiddevs.mvvmnewsapp.ui.uii.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.uii.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.util.Constants
import com.androiddevs.mvvmnewsapp.ui.util.Constants.Companion.SEARCH_TIME_DELAY
import com.androiddevs.mvvmnewsapp.ui.util.Resource
import com.androiddevs.mvvmnewsapp.ui.viewModel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SeachNewsFragment:Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter:NewsAdapter
    var isLoading = false
    var isLastPage= false
    var isScrolling = false
    val TAG ="SearchNewsFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        var job: Job? = null
        etSearch?.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                editable.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.getSearchNews(editable.toString())
                    }
                }
            }

        }

        newsAdapter.setOnItemListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_seachNewsFragment_to_articleFragment,bundle)

        }



        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val  totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE +2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if(isLastPage){
                            rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.messege?.let { message ->
                        Log.i(TAG, "An error ocurred: $message")
                        Toast.makeText(activity, "An error ocurred: $message", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
        private fun setupRecyclerView(){
            newsAdapter = NewsAdapter()
            rvSearchNews.apply {
                adapter= newsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(this@SeachNewsFragment.scrollListner)
            }
        }



    //cmtrl + O to override te methods
    val scrollListner = object : RecyclerView.OnScrollListener(){

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutMAnager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutMAnager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutMAnager.childCount
            val totalItemCount = layoutMAnager.itemCount
            // check is scrolled to the bottom of recyecler

            val  isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtTheBeginning = firstVisibleItemPosition >=0
            val isTotalMorethanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isNotAtTheBeginning &&isAtLastItem  && isTotalMorethanVisible && isScrolling
            if (shouldPaginate){
                viewModel.getSearchNews(etSearch.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }


    private fun showProgressBar(){
            paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
        }

        private fun hideProgressBar(){
            paginationProgressBar.visibility = View.INVISIBLE
            isLoading =false
        }



}