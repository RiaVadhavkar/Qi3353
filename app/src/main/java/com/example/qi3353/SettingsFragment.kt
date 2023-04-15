package com.example.qi3353

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Switch
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentSettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
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


class SettingsFragment : Fragment() {
    val TAG = "SettingsFragment"

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseService.sharedPref = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = it
            //binding.etToken.setText(it)


        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)


        var view = binding.root

        var notificationSwitch = view.findViewById<Switch>(R.id.notificationSwitch)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user == null) {
            binding.logoutButton.text = "Sign In"
            binding.preferencesButton.visibility = View.GONE
        }


        val preferences = view.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE)
        var temp: Boolean = false
        var preferencesList: Boolean =
            preferences.getBoolean(FirebaseMessaging.getInstance().token.toString(), temp)

        if (temp)
        {
            binding.notificationSwitch.isChecked = true
        }
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        client = GoogleSignIn.getClient(view.context, options)

        binding.navigation.homeBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_settingsFragment_to_forYouFragment)
        }
        binding.navigation.searchBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_settingsFragment_to_searchFragment)
        }
        binding.preferencesButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_settingsFragment_to_preferencesFragment)
        }
        binding.logoutButton.setOnClickListener {
            if (user != null) {
                auth.signOut()
                client.signOut()
            }
            view.findNavController().navigate(R.id.action_settingsFragment_to_signInFragment)
        }

        val spinner = binding.reminderSpinner
        ArrayAdapter.createFromResource(
            activity as Context,
            R.array.reminderTimes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }

        notificationSwitch.setOnCheckedChangeListener { view, isChecked ->
            var recipientToken = FirebaseMessaging.getInstance().token.toString()
            if (isChecked){
                if(recipientToken.isNotEmpty()) {
                    val preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                    var editor = preferences.edit()

                    // "key: email", "value : set of preferences"
                    editor.putBoolean (recipientToken, isChecked)
                    editor.commit()
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
                                //var stop = 0
                                var changedEventCount = 0
                                children.forEach {
                                    //Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                                    //if (stop == 0) {
                                    Log.e("test", "line 71")

                                    val body = dataSnapshot.child("body").value.toString()
                                    if (JSONObject(it.getValue(String::class.java)!!).has("isNew")) {
                                    isChanged = JSONObject(it.getValue(String::class.java)!!).get("isNew").toString()
                                    if (isChanged == "yes") {
                                        //title = dataSnapshot.child("title").value.toString()
                                        /*title =
                                            JSONObject(it.getValue(String::class.java)!!).get("organization")
                                                .toString()
                                        message = JSONObject(it.getValue(String::class.java)!!).get("name")
                                            .toString() + " is hosting an event!"*/
                                        title = "Qi3353 Has New Events"
                                        message = "" + changedEventCount + " new events have been added!"
                                        isChanged = "no"
                                        //stop = 1
                                        changedEventCount = changedEventCount + 1

                                    }
                                    }
                                //}



                                }
                                //val message = binding.etMessage.text.toString()
                                //demo key:
                                // fnhm0gU7S6m_NZOwueBrLP:APA91bGPBHmwZVU5SVavmWLRNKsE8tgVzOy4VTGVQPXru6k5VCQzUGojrYN-zmaM8Mzf2jTh2aP5oDkt-XpjQxSmsCRdL8uAMH8lOH4dhtVuS0rwe66h4BRqzyD_zdCtNGOPEKEQd6bq
                                //val recipientToken = binding.etToken.text.toString()
                                val recipientToken = "fnhm0gU7S6m_NZOwueBrLP:APA91bGPBHmwZVU5SVavmWLRNKsE8tgVzOy4VTGVQPXru6k5VCQzUGojrYN-zmaM8Mzf2jTh2aP5oDkt-XpjQxSmsCRdL8uAMH8lOH4dhtVuS0rwe66h4BRqzyD_zdCtNGOPEKEQd6bq"



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
                }
                //view.findNavController().navigate(R.id.action_settingsFragment_to_notificationTokenFragment);
            }
        }

        return view
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



    //notificationSwitch
}