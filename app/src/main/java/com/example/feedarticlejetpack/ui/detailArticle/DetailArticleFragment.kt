package com.example.feedarticlejetpack.ui.detailArticle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.databinding.FragmentDetailArticleBinding
import com.example.feedarticlejetpack.utils.parsedDate
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailArticleFragment : Fragment() {

    private var _binding: FragmentDetailArticleBinding? = null
    private val vm: DetailArticleViewModel by viewModels()
    private val args: DetailArticleFragmentArgs by navArgs()
    private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailArticleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.userMessage.observe(viewLifecycleOwner){
            Toast.makeText(view.context, getString(it), Toast.LENGTH_SHORT).show()
        }

        args.article.let {
            when(it.category){
                1 -> R.string.sport
                2 -> R.string.manga
                3 -> R.string.various
                else -> return
            }.let { cat ->
                binding.tvDetailArticleDate.text =
                    getString(R.string.created_at, parsedDate(it.createdAt))
                binding.tvDetailArticleCategory.text =
                    getString(R.string.category_type, getString(cat))
                binding.tvDetailArticleArticleTitle.text = it.title
                binding.tvDetailArticleDescription.text = it.description

                when(it.isFav){
                    1 -> {
                        binding.ivDetailArticleStarEmpty.visibility = View.GONE
                        binding.ivDetailArticleStarFill.visibility = View.VISIBLE
                    }
                }
                it.urlImage.let {url ->
                    Picasso
                        .get()
                        .load(url.ifEmpty { "boo" })
                        .error(R.drawable.feedarticles_logo)
                        .into(binding.ivDetailArticleImage)
                }
                binding.ivDetailArticleStarEmpty.setOnClickListener { _ ->
                    vm.toggleFavorite(it.id)
                }
                binding.ivDetailArticleStarFill.setOnClickListener { _ ->
                    vm.toggleFavorite(it.id)
                }
                vm.setIsFavorite(it.isFav)
            }
        }

        binding.btnDetailArticleBack.setOnClickListener {
            findNavController().navigate(R.id.action_detailArticleFragment_to_homeFragment)
        }

        vm.isFavorite.observe(viewLifecycleOwner){
            binding.ivDetailArticleStarFill.visibility = if (it) View.VISIBLE else View.GONE
            binding.ivDetailArticleStarEmpty.visibility = if (it) View.GONE else View.VISIBLE
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}