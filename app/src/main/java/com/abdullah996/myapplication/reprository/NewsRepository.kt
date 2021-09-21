package com.abdullah996.myapplication.reprository

import com.abdullah996.myapplication.api.RetrofitInstance


class NewsRepository {
    suspend fun getTopNews()=
        RetrofitInstance.api.getTopNew()

    suspend fun searchNews(searchQuery: String) =
        RetrofitInstance.api.searchNews(searchQuery)

}