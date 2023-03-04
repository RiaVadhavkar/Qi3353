package com.example.qi3353

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentLoadingBinding

class LoadingFragment : Fragment() {
    private lateinit var binding: FragmentLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadingBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.signUp.setOnClickListener{
            view.findNavController().navigate(R.id.loadingFragment)
        }

        return view
    }
}