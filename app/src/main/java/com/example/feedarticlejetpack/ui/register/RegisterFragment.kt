package com.example.feedarticlejetpack.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding
        get() = _binding!!
    private val vm : RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegisterRegister.setOnClickListener {
            vm.registerUser(
                binding.etRegisterLogin.text.toString(),
                binding.etRegisterPassword.text.toString(),
                binding.etRegisterConfirmPassword.text.toString()
            )
        }

        vm.isRegister.observe(viewLifecycleOwner){
            if(it)
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)

        }

        vm.userMessage.observe(viewLifecycleOwner){
            Toast.makeText(view.context, getString(it), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}