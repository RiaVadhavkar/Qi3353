package com.example.qi3353


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.qi3353.databinding.NotificationTokenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import org.json.JSONObject

const val TOPIC = "/topics/myTopic"

class NotificationTokenFragment : Fragment() {
    private val model: EventViewModel by viewModels()

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
        Log.e("test", "line 52")
        //binding.btnSend.setOnClickListener {
            var title = "" //= "oh crap" //binding.etTitle.text.toString()
            var message = ""
            var isChanged = ""
            // Create the observer which updates the UI.
            //val nameObserver = Observer<MutableList<Event>> {
            //}

            // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
            //model.eventListLiveData.observe(this, nameObserver)
            Log.e("test", "line 61")
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val reference = firebaseDatabase.getReference()
            reference.child("Events")//"myTopic/topic-name")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val children = dataSnapshot.children

                        children.forEach {
                            //Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                            Log.e("test", "line 71")

                            val body = dataSnapshot.child("body").value.toString()
                            isChanged = JSONObject(it.getValue(String::class.java)!!).get("isNew").toString()
                            if (isChanged == "yes") {
                                //title = dataSnapshot.child("title").value.toString()
                                title =
                                    JSONObject(it.getValue(String::class.java)!!).get("organization")
                                        .toString()
                                message = JSONObject(it.getValue(String::class.java)!!).get("name")
                                    .toString() + " is hosting an event!"
                                isChanged = "no"
                            }
                        }
                        //val message = binding.etMessage.text.toString()
                        //demo key:
                        // fnhm0gU7S6m_NZOwueBrLP:APA91bGPBHmwZVU5SVavmWLRNKsE8tgVzOy4VTGVQPXru6k5VCQzUGojrYN-zmaM8Mzf2jTh2aP5oDkt-XpjQxSmsCRdL8uAMH8lOH4dhtVuS0rwe66h4BRqzyD_zdCtNGOPEKEQd6bq
                        val recipientToken = binding.etToken.text.toString()




                        Log.e("test", "line 88")
                        if (title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                            PushNotification(NotificationData(title, message), recipientToken).also {
                                sendNotification(it)
                                Log.e("test", "line 92")
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                }
                )




        //}


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
                Log.e("test", "test else statement")
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
            Log.e("test", "catch clause")
        }
    }


}

