package com.abdullah996.myapplication.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdullah996.myapplication.R
import com.abdullah996.myapplication.models.Article
import com.abdullah996.myapplication.ui.MainActivity
import com.abdullah996.myapplication.viewmodels.NewsViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment:Fragment(R.layout.fragment_details) {
    private lateinit var viewModel: NewsViewModel
    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
       val article = args.article
        setupFragment(article)
        setListeners()
    }

    private fun setupFragment(article: Article) {
        Glide.with(this).load(article.urlToImage).into(details_image)
        source_text.text=article.source.name
        title_text.text=article.title
        desc_text.text=article.description
    }
    private  fun setListeners(){
        button_back.setOnClickListener {
            val action=DetailsFragmentDirections.actionDetailsFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}