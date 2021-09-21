package com.abdullah996.myapplication.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abdullah996.NewsApplication
import com.abdullah996.myapplication.models.ApiResponse
import com.abdullah996.myapplication.reprository.NewsRepository
import com.abdullah996.myapplication.util.Resource

import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class NewsViewModel(
        app:Application,
        val newsReposiroty: NewsRepository
):AndroidViewModel(app) {

    val topNews:MutableLiveData<Resource<ApiResponse>> = MutableLiveData()
    val searchNews:MutableLiveData<Resource<ApiResponse>> = MutableLiveData()



    init {
        getTopNews()
    }
    fun getTopNews()=viewModelScope.launch {
        safeTopNewsCall()

    }
    fun searchNews(searchQuery:String)=viewModelScope.launch {
        safeSearchNewsCall(searchQuery)

    }

    private fun handleTopNewsResponse(response:Response<ApiResponse>):Resource<ApiResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }

        }
        return Resource.Error(response.message())
    }
    private fun handleSearchNewsResponse(response:Response<ApiResponse>):Resource<ApiResponse>{
        if (response.isSuccessful){
            response.body()?.let {resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private suspend fun safeTopNewsCall(){
        topNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response=newsReposiroty.getTopNews()
                topNews.postValue(handleTopNewsResponse(response))
            }else{
                topNews.postValue(Resource.Error("no internet connection"))
            }
        }catch (t:Throwable){
            when(t) {
                is IOException ->topNews.postValue(Resource.Error("network failure"))
                else -> topNews.postValue(Resource.Error("conversion error"))
            }
        }
    }
    private suspend fun safeSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response=newsReposiroty.searchNews(searchQuery)
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resource.Error("no internet connection"))
            }
        }catch (t:Throwable){
            when(t) {
                is IOException->searchNews.postValue(Resource.Error("network failure"))
                else -> searchNews.postValue(Resource.Error("conversion error"))
            }
        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager=getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            val activeNetwork=connectivityManager.activeNetwork ?:return false
            val capability=connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)-> true
                capability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                capability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)-> true
                else->false
            }

        }
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI ->true
                    ConnectivityManager.TYPE_MOBILE ->true
                    ConnectivityManager.TYPE_ETHERNET ->true
                    else->false
                }
            }
        }
        return false
    }

}