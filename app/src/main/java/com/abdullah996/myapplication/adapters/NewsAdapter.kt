package com.abdullah996.myapplication.adapters

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdullah996.myapplication.R
import com.abdullah996.myapplication.models.Article
import com.bumptech.glide.Glide



import kotlinx.android.synthetic.main.item_news_card.view.*

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.DetailsViewHolder>() {
    inner class DetailsViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    private val diffCallBack=object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }
    val differ=AsyncListDiffer(this,diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {

        return DetailsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_news_card,
                parent,
                false
            )
        )
    }

    private var onItemClickListener:((Article)->Unit)?=null

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val  article=differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(details_image)
            source_text.text = article.source.name
            title_text.text = article.title

            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }



    fun setOnClickListeners(listener:(Article)->Unit){
        onItemClickListener=listener
    }
}