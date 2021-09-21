package com.abdullah996.myapplication.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdullah996.myapplication.R
import com.abdullah996.myapplication.adapters.NewsAdapter
import com.abdullah996.myapplication.ui.MainActivity
import com.abdullah996.myapplication.viewmodels.NewsViewModel
import com.abdullah996.myapplication.util.Resource
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment:Fragment(R.layout.fragment_home) {
    private  lateinit var viewModel: NewsViewModel
    private  lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        newsAdapter.setOnClickListeners {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailsFragment,
                bundle
            )
        }
        var job:Job?=null
        etSearch.addTextChangedListener {editable->
            job?.cancel()
            job= MainScope().launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                    else{
                        topNews()
                    }

                }
            }
        }
        topNews()

        searchNews()




    }
   private fun topNews(){
        viewModel.topNews.observe(viewLifecycleOwner,  { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"an error occurred : $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }
   private fun searchNews(){
        viewModel.searchNews.observe(viewLifecycleOwner,  { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"an error occurred : $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }
    private fun hideProgressBar() {
        home_progress_bar.visibility = View.INVISIBLE

    }

    private fun showProgressBar() {
        home_progress_bar.visibility = View.VISIBLE

    }
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        home_recycle_view.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

}