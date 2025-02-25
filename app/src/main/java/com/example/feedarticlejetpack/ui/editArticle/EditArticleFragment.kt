package com.example.feedarticlejetpack.ui.editArticle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.databinding.FragmentEditArticleBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditArticleFragment : Fragment() {
    private var _binding: FragmentEditArticleBinding? = null
    private val binding
        get() = _binding!!

    private val vm: EditArticleViewModel by viewModels()

    private val args: EditArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.userMessage.observe(viewLifecycleOwner){
            Toast.makeText(view.context, getString(it), Toast.LENGTH_SHORT).show()
        }

        args.article.let {
            binding.etEditArticleArticleTitle.setText(it.title)
            binding.etEditArticleDescription.setText(it.description)
            binding.etEditArticleImageUrl.setText(it.urlImage)
            binding.rgAddArticleCat.check(
                when(it.category){
                    1 -> R.id.rb_editArticle_sport
                    2 -> R.id.rb_editArticle_manga
                    3 -> R.id.rb_editArticle_various
                    else -> return
                }
            )
            Picasso
                .get()
                .load(it.urlImage.ifEmpty { "boo" })
                .error(R.drawable.feedarticles_logo)
                .into(binding.ivEditArticleImage)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}