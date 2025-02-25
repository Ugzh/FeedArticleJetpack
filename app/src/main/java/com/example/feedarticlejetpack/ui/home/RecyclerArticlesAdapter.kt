package com.example.feedarticlejetpack.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.databinding.ItemRvArticleBinding
import com.example.feedarticlejetpack.network.dtos.ArticleDto
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class RecyclerArticlesAdapter(): ListAdapter<ArticleDto, RecyclerArticlesAdapter.ArticleHolder>(MyDiffUtil()) {
    inner class ArticleHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ItemRvArticleBinding.bind(itemView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_article, parent, false)
            .let {
                return ArticleHolder(it)
            }
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val parserDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        val formatterDate = SimpleDateFormat("dd/MM/yyyy");
        getItem(position).let {
            with(holder.binding){

                Picasso.get().load(it.urlImage).error(R.drawable.feedarticles_logo).into(ivRvImage)
                tvRvTitle.text = it.title
                tvRvDate.text = formatterDate.format(parserDate.parse(it.createdAt)!!);
                tvRvDescription.text = it.description
            }
        }

    }
}

class MyDiffUtil: DiffUtil.ItemCallback<ArticleDto>() {
    override fun areItemsTheSame(oldItem: ArticleDto, newItem: ArticleDto) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ArticleDto, newItem: ArticleDto) =
        oldItem == newItem


}