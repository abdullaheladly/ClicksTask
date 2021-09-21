package com.abdullah996.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.abdullah996.myapplication.R
import com.abdullah996.myapplication.reprository.NewsRepository
import com.abdullah996.myapplication.viewmodels.NewsViewModel
import com.abdullah996.myapplication.viewmodels.NewsViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository=NewsRepository()
        val viewModelProviderFactory=NewsViewModelProviderFactory(application,repository)
        viewModel=ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

    }
}