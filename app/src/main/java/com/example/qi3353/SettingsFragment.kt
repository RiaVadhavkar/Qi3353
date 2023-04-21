package com.example.qi3353

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit


const val TOPIC = "/topics/myTopic"


class SettingsFragment : Fragment() {
    val TAG = "SettingsFragment"

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient
    private lateinit var notificationSwitch: Switch //= binding.notificationSwitch
    private lateinit var reminderSwitch: Switch //= binding.notificationSwitch
    private lateinit var spinner: Spinner

    private lateinit var sharedPrefs: SharedPreferences

    var switchState2 = false
    var switchState3 = false
    var timeDiff = 1800
    var selectedState = 0

    //private var temp: Boolean = false
    //private var clickedPref: Boolean = binding.notificationSwitch.isChecked()
    //private var temp: Boolean = false
    //private var preferencesList: Boolean =
    //    getBoolean(FirebaseMessaging.getInstance().token.toString(), temp)
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

        binding.pageTitle.title.text = "Settings"


        var view = binding.root

        notificationSwitch = view.findViewById<Switch>(R.id.notificationSwitch)
        reminderSwitch = view.findViewById<Switch>(R.id.reminderSwitch)
        spinner = view.findViewById<Spinner>(R.id.reminderSpinner)


        //temp = true
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user == null) {
            binding.logoutButton.text = "Sign In"
            binding.userContainer.visibility = View.GONE
            binding.preferencesButton.visibility = View.GONE
        }
        else {
            binding.email.text = user.email.toString()
        }

        sharedPrefs = view.context.getSharedPreferences("com.example.qi3353", Context.MODE_PRIVATE)
        notificationSwitch.setChecked(sharedPrefs.getBoolean("isChecked", false))
        switchState2 = sharedPrefs.getBoolean("isChecked", false);

        reminderSwitch.setChecked(sharedPrefs.getBoolean("isChecked1", false))
        switchState3 = sharedPrefs.getBoolean("isChecked1", false);

        spinner.setSelection(sharedPrefs.getInt("selected", 0));
        selectedState = sharedPrefs.getInt("selected", 0)
        //var preferencesList: Boolean =
            //preferences.getBoolean(FirebaseMessaging.getInstance().token.toString(), temp)

        /*if (temp)
        {
            notificationSwitch.isChecked = true
        }*/
        /*preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        var editor = preferences.edit()

        // "key: email", "value : set of preferences"
        editor.putBoolean(recipientToken, true)
        editor.commit()*/
        //notificationSwitch.setOnClickListener(View.OnClickListener {
            /*val editor: SharedPreferences.Editor = preferences.edit()
            editor.putBoolean("notiftoggleButton", notificationSwitch.isChecked())
            //temp = true
            editor.commit()*/
        //})
        //notificationSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

        //})
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
            R.layout.custom_spinner_selected_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            spinner.adapter = adapter
        }

        spinner.setSelection(getPersistedItem());
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: View?,
                position: Int,
                itemId: Long
            ) {
                setPersistedItem(position)
                if(position == 2) {
                    timeDiff = 86400
                    val editor = sharedPrefs.edit()
                    editor.putInt("selected", 2)
                    editor.commit()
                    selectedState = 2


                    //Log.e("text", "one day selected")
                }

                if(position == 1) {
                    timeDiff = 3600
                    val editor = sharedPrefs.edit()
                    editor.putInt("selected", 1)
                    editor.commit()
                    selectedState = 1
                    //Log.e("text", "one hour selected")

                }
                else {
                    timeDiff = 1800
                    val editor = sharedPrefs.edit()
                    editor.putInt("selected", 0)
                    editor.commit()
                    selectedState = 0
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        })

        if (notificationSwitch.isChecked) {
            notificationSwitch.thumbTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
            notificationSwitch.trackTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
        }

        if (reminderSwitch.isChecked) {
            reminderSwitch.thumbTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
            reminderSwitch.trackTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
        }

//        spinner.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
//                if(pos == 2) {
//                    timeDiff = 86400
//                    val editor = sharedPrefs.edit()
//                    editor.putInt("selected", 2)
//                    editor.commit()
//                    selectedState = 2
//
//
//                    //Log.e("text", "one day selected")
//                }
//
//                if(pos == 1) {
//                    timeDiff = 3600
//                    val editor = sharedPrefs.edit()
//                    editor.putInt("selected", 1)
//                    editor.commit()
//                    selectedState = 1
//                    //Log.e("text", "one hour selected")
//
//                }
//                else {
//                    timeDiff = 1800
//                    val editor = sharedPrefs.edit()
//                    editor.putInt("selected", 0)
//                    editor.commit()
//                    selectedState = 0
//                }
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {
//                /*timeDiff = 1800
//                val editor = sharedPrefs.edit()
//                editor.putInt("selected", 0)
//                editor.commit()
//                selectedState = 0*/
//            }
//        }


        notificationSwitch.setOnCheckedChangeListener { view, isChecked ->
            var recipientToken = FirebaseMessaging.getInstance().token.toString()

            if (isChecked){
                    val editor = sharedPrefs.edit()
                    editor.putBoolean("isChecked", true)
                    editor.commit()
                    switchState2 = true

                notificationSwitch.thumbTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
                notificationSwitch.trackTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)

                //temp = true
                if(recipientToken.isNotEmpty()) {
                    //preferences = requireActivity().getSharedPreferences("isChecked", Context.MODE_PRIVATE)
                    //var editor = preferences.edit()

                    // "key: email", "value : set of preferences"
                    //editor.putBoolean(recipientToken, isChecked)
                    //editor.putBoolean("isChecked", true).apply()
                    //editor.commit()
                    var title = "" //= "oh crap" //binding.etTitle.text.toString()
                    var message = ""
                    var isChanged = ""
                    // Create the observer which updates the UI.
                    //val nameObserver = Observer<MutableList<Event>> {
                    //}

                    // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
                    //model.eventListLiveData.observe(this, nameObserver)
                    //Log.e("test", "line 61")
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
                                val recipientToken = "fCMEZJKPTCG8bmePLsc_Tu:APA91bE8R3mdvPCLJMLGSwagmeSB9QJksdA9VKwSy45-nJUxrAkkJqTmj8UTjWh9gflOgif6SuOrKepaJZyewLsd-XDfj5O1rWuT5JYJqujUU0XUUoM_xaJG52u60P4s_5uggNQAz1kF"
                                //"fnhm0gU7S6m_NZOwueBrLP:APA91bGPBHmwZVU5SVavmWLRNKsE8tgVzOy4VTGVQPXru6k5VCQzUGojrYN-zmaM8Mzf2jTh2aP5oDkt-XpjQxSmsCRdL8uAMH8lOH4dhtVuS0rwe66h4BRqzyD_zdCtNGOPEKEQd6bq"



                                Log.e("test", "line 88")
                                if (title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                                    PushNotification(NotificationData(title, message), recipientToken).also {
                                        sendNotification(it)
                                        //Log.e("test", "line 92")
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
            else {
                val editor: SharedPreferences.Editor =
                    view.context.getSharedPreferences("com.example.qi3353", MODE_PRIVATE).edit()
                editor.putBoolean("isChecked", false)
                editor.commit()
                switchState2 = false

                notificationSwitch.thumbTintList = ContextCompat.getColorStateList(requireContext(), R.color.lightGray)
                notificationSwitch.trackTintList = ContextCompat.getColorStateList(requireContext(), R.color.lightGray)

            }
        }










        //////////////////////////REMINDERS/////////////////////////////////////
        reminderSwitch.setOnCheckedChangeListener { view, isChecked1 ->
            var recipientToken = FirebaseMessaging.getInstance().token.toString()

            if (isChecked1){
                val editor = sharedPrefs.edit()
                editor.putBoolean("isChecked1", true)
                editor.commit()
                switchState3 = true

                reminderSwitch.thumbTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
                reminderSwitch.trackTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)


                if(recipientToken.isNotEmpty()) {

                    var otherTitle = "" //= "oh crap" //binding.etTitle.text.toString()
                    var otherMessage = ""
                    //var isChanged = ""

                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val reference = firebaseDatabase.getReference()
                    //val x = reference.child("Events")
                    //Log.e("test", "THIS IS LINE 292")
                    reference.child("Events")//"myTopic/topic-name")
                        .addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val children = dataSnapshot.children
                                //var stop = 0
                                //var changedEventCount = 0
                                children.forEach {

                                    val body = dataSnapshot.child("body").value.toString()
                                    if(JSONObject(it.getValue(String::class.java)!!).has("startRaw")) {
                                    //if (JSONObject(it.getValue(String::class.java)!!).has("isNew")) {
                                        var str_date =
                                            JSONObject(it.getValue(String::class.java)!!).get("startRaw")
                                        val zonedDateTime = ZonedDateTime.parse(str_date.toString())
                                        val scheduledSeconds = zonedDateTime.toEpochSecond()
                                        val now = ZonedDateTime.parse(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).toString()+":00+00:00")//now()

                                        val nowSeconds = now.toEpochSecond()
//                                        val period: Period = Period.between(
//                                            now.toLocalDate(),
//                                            zonedDateTime.toLocalDate()
//                                        )
                                        //val thresholdPeriod: Period = 3600
                                        //)
                                        //if (scheduledSeconds != null && nowSeconds != null) {
                                            //if (period.days == 0 && period.months == 0 && period.years == 0) {
                                        //Log.e("test", "line MADE IT HERE: " + (scheduledSeconds - nowSeconds))
                                        //Log.e("test", "line MADE IT HERE: " + zonedDateTime.truncatedTo(ChronoUnit.HOURS) + " AND " + now.truncatedTo(ChronoUnit.HOURS))
                                        if (scheduledSeconds != null && nowSeconds != null && (scheduledSeconds - nowSeconds <= timeDiff)  && (scheduledSeconds - nowSeconds >= 0)) {
                                        //if (zonedDateTime)
                                        //if (zonedDateTime.truncatedTo(ChronoUnit.HOURS)
                                        //                .isEqual(now.truncatedTo(ChronoUnit.HOURS))) {
                                                    //    isChanged = JSONObject(it.getValue(String::class.java)!!).get("isNew").toString()
                                                //    if (isChanged == "yes") {
                                                    //Log.e("test", "line MADE IT HERE")
                                                    var orgName =
                                                        JSONObject(it.getValue(String::class.java)!!).get("organization")
                                            var eventName =
                                                JSONObject(it.getValue(String::class.java)!!).get("name")

                                                otherTitle = "" + orgName + " is hosting an event soon!"
                                                otherMessage = "" + eventName
                                                //isChanged = "no"
                                                //stop = 1
                                                //changedEventCount = changedEventCount + 1

                                            }

                                    //}
                                    }



                                }
                                //val message = binding.etMessage.text.toString()
                                //demo key:
                                // fnhm0gU7S6m_NZOwueBrLP:APA91bGPBHmwZVU5SVavmWLRNKsE8tgVzOy4VTGVQPXru6k5VCQzUGojrYN-zmaM8Mzf2jTh2aP5oDkt-XpjQxSmsCRdL8uAMH8lOH4dhtVuS0rwe66h4BRqzyD_zdCtNGOPEKEQd6bq
                                //val recipientToken = binding.etToken.text.toString()
                                val recipientToken = "dDu3HbTJSciPz_f51gFUkp:APA91bHHufCPg60CgkAzgtiwIeyIqDQmxcCNSBRM-F50QhCLmTDdm7KSbqrr8sTFn5EqeNl48o9980hwRXYQzb8TDUUicYcQflPG4zxmzRDZ4os5aEonXzeXCes_cvX06liYkPmM5ChD"



                                Log.e("test", "line 88")
                                if (otherTitle.isNotEmpty() && otherMessage.isNotEmpty() && recipientToken.isNotEmpty()) {
                                    PushNotification(NotificationData(otherTitle, otherMessage), recipientToken).also {
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
            else {
                val editor: SharedPreferences.Editor =
                    view.context.getSharedPreferences("com.example.qi3353", MODE_PRIVATE).edit()
                editor.putBoolean("isChecked1", false)
                editor.commit()
                switchState3 = false

                reminderSwitch.thumbTintList = ContextCompat.getColorStateList(requireContext(), R.color.lightGray)
                reminderSwitch.trackTintList = ContextCompat.getColorStateList(requireContext(), R.color.lightGray)

            }
        }


        ////////////////////////////////////////////////////////////////////////











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


    private fun getPersistedItem(): Int {
        val keyName = makePersistedItemKeyName()
        return sharedPrefs.getInt(keyName, 0)
    }

    protected fun setPersistedItem(position: Int) {
        val keyName = makePersistedItemKeyName()
        sharedPrefs.edit().putInt(keyName, position)
            .commit()
    }

    private fun makePersistedItemKeyName(): String {
        return "_your_key"
    }
    //notificationSwitch
}