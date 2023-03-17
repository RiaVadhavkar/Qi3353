package com.example.qi3353

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.switch1.setOnClickListener(View.OnClickListener {
            binding.switch1.text = "Push Notifications"+ binding.switch1.isChecked
    })

        view.addView(binding.switch1)

        binding.switch2.setOnClickListener(View.OnClickListener {
            binding.switch2.text = "Set Reminders"+ binding.switch2.isChecked
        })

        view.addView(binding.switch2)


        return view
    }


}