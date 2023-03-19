package com.example.qi3353

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        var view = binding.root

        // Bottom navigation buttons.
        binding.homeBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_profileFragment_to_forYouFragment)
        }
        binding.searchBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_profileFragment_to_searchFragment)
        }

        binding.notificationBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_profileFragment_to_notificationFragment)
        }

        return view
    }
}