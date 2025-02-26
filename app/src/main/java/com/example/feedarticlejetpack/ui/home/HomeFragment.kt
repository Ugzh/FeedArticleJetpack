package com.example.feedarticlejetpack.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.databinding.FragmentHomeBinding
import com.example.feedarticlejetpack.network.dtos.ArticleDto
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var articlesAdapter: RecyclerArticlesAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private val vm : HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivHomeLogout.setOnClickListener {
            vm.disconnectUser()
        }


        binding.rvHomeArticles.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = articlesAdapter.apply {
                setGetIdArticleOnClickCallBack {
                    vm.openEditOrDetailFragment(it)
                }
            }
        }

        vm.navDirectionLiveData.observe(viewLifecycleOwner){
            it?.let {
                findNavController().navigate(it)
                vm.resetNavDirectionLiveData()
            }
        }

        vm.articlesListLiveData.observe(viewLifecycleOwner){
            articlesAdapter.submitList(it){
                binding.rvHomeArticles.scrollToPosition(0)
            }
        }

        vm.userMessage.observe(viewLifecycleOwner){
            Toast.makeText(view.context, getString(it), Toast.LENGTH_SHORT).show()
        }

        vm.isLogout.observe(viewLifecycleOwner){
            if(it)
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        binding.ivHomeAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addArticleFragment)
        }

        binding.rgHomeCategories.setOnCheckedChangeListener{ rg, _ ->
            vm.setCategoryToFilter(rg.checkedRadioButtonId)
        }

        vm.isFavoriteChecked.observe(viewLifecycleOwner){
            binding.ivHomeFavoriteEmpty.visibility = if (it) View.GONE else View.VISIBLE
            binding.ivHomeFavoriteFill.visibility = if (it) View.VISIBLE else View.GONE
            vm.setCategoryToFilter(binding.rgHomeCategories.checkedRadioButtonId)

        }

        binding.ivHomeFavoriteFill.setOnClickListener {
            vm.toggleFavoriteChecked()
        }
        binding.ivHomeFavoriteEmpty.setOnClickListener {
            vm.toggleFavoriteChecked()
        }

        binding.swHome.setOnRefreshListener {
            vm.getAllArticles()
            binding.swHome.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        vm.getAllArticles()
        
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}