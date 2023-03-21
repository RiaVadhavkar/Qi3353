package com.example.qi3353

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val view = binding.root

        // Bottom navigation buttons.
        binding.navigation.homeBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_notificationFragment_to_forYouFragment)
        }
        binding.navigation.searchBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_notificationFragment_to_searchFragment)
        }
        binding.navigation.profileBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_notificationFragment_to_profileFragment)
        }

//        binding.switch1.setOnClickListener(View.OnClickListener {
//            binding.switch1.text = "Push Notifications"+ binding.switch1.isChecked
//    })
//
//        view.addView(binding.switch1)
//
//        binding.switch2.setOnClickListener(View.OnClickListener {
//            binding.switch2.text = "Set Reminders"+ binding.switch2.isChecked
//        })
//
//        view.addView(binding.switch2)


        return view
    }


}