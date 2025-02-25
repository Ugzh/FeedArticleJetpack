package com.example.feedarticlejetpack.ui.addArticle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.databinding.FragmentAddArticleBinding
import com.example.feedarticlejetpack.databinding.FragmentRegisterBinding
import com.example.feedarticlejetpack.ui.register.RegisterViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddArticleFragment : Fragment() {

    private var _binding: FragmentAddArticleBinding? = null
    private val binding
        get() = _binding!!

    private val vm: AddArticleViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddArticleSave.setOnClickListener {
            vm.addArticle(
                binding.etAddArticleArticleTitle.text.toString(),
                binding.etAddArticleDescription.text.toString(),
                binding.etAddArticleImageUrl.text.toString(),
                binding.rgAddArticleCat.checkedRadioButtonId)
        }

        vm.userMessage.observe(viewLifecycleOwner){
            Toast.makeText(view.context, getString(it), Toast.LENGTH_SHORT).show()
        }

        vm.isArticleCreated.observe(viewLifecycleOwner){
            if(it)
                findNavController().navigate(R.id.action_addArticleFragment_to_homeFragment)
        }

        binding.etAddArticleImageUrl.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                val url = binding.etAddArticleImageUrl.text.toString().trim()
                Picasso
                    .get()
                    .load(url.ifEmpty { "boo" })
                    .error(R.drawable.feedarticles_logo)
                    .into(binding.ivAddArticleImage)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}