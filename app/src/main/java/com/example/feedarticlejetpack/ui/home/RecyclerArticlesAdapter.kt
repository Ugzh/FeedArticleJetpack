package com.example.feedarticlejetpack.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.databinding.ItemRvArticleBinding
import com.example.feedarticlejetpack.network.dtos.ArticleDto
import com.example.feedarticlejetpack.utils.parsedDate
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class RecyclerArticlesAdapter : ListAdapter<ArticleDto, RecyclerArticlesAdapter.ArticleHolder>(MyDiffUtilArticle()) {

    private var getIdArticleOnClickCallBack: ((Long) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_article, parent, false)
            .let {
                return ArticleHolder(it)
            }
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        getItem(position).let {
            with(holder.binding){
                Picasso.get().load(it.urlImage.ifEmpty { "boo" }).error(R.drawable.feedarticles_logo).into(ivRvImage)
                tvRvTitle.text = it.title
                tvRvDate.text = parsedDate(it.createdAt)
                tvRvDescription.text = it.description

                when(it.category){
                    1 -> R.color.beige
                    2 -> R.color.pink
                    3 -> R.color.yellow
                    else -> return
                }.let {
                    cvItemRv.setCardBackgroundColor(holder.itemView.resources.getColor(it))
                }

                cvItemRv.setOnClickListener { _ ->
                    getIdArticleOnClickCallBack?.invoke(it.id)
                }

                if (it.isFav == 1){
                    ivRvFavorite.visibility = View.VISIBLE
                } else
                    ivRvFavorite.visibility = View.GONE
            }
        }
    }

    fun setGetIdArticleOnClickCallBack(getIdArticleOnClickCallBack: (Long) -> Unit){
        this.getIdArticleOnClickCallBack = getIdArticleOnClickCallBack
    }

    inner class ArticleHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ItemRvArticleBinding.bind(itemView)
    }
}

class MyDiffUtilArticle: DiffUtil.ItemCallback<ArticleDto>() {
    override fun areItemsTheSame(oldItem: ArticleDto, newItem: ArticleDto) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ArticleDto, newItem: ArticleDto) =
        oldItem == newItem
}