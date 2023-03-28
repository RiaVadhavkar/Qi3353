package com.example.qi3353

import android.content.Context
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentSettingsBinding

import java.util.*

import com.example.qi3353.databinding.NotificationTokenBinding
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class NotificationTokenFragment : Fragment() {
    val TAG = "NotificationTokenFragment"
    private lateinit var binding: NotificationTokenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseService.sharedPref = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = it
            binding.etToken.setText(it)


        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationTokenBinding.inflate(inflater, container, false)
        var view = binding.root
        binding.btnSend.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val message = binding.etMessage.text.toString()
            val recipientToken = binding.etToken.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(NotificationData(title, message), recipientToken).also {
                    sendNotification(it)
                }
            }

        }


        return view;
    }
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch{
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")

            }
            else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

}

