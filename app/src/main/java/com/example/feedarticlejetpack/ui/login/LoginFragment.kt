package com.example.feedarticlejetpack.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.databinding.FragmentLoginBinding
import com.example.feedarticlejetpack.network.Prefs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment @Inject constructor(): Fragment(){

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!
    private val vm : LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLoginLogin.setOnClickListener {
            vm.logUser(
                binding.etLoginUsername.text.toString(),
                binding.etLoginPassword.text.toString()
            )
        }

        //vm.isLogged.observe()
        vm.userMessage.observe(viewLifecycleOwner){
            Toast.makeText(view.context, getString(it), Toast.LENGTH_SHORT).show()
        }

        binding.tvLoginNoAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}