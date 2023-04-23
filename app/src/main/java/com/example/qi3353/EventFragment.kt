package com.example.qi3353

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentEventBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.CalendarScopes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import java.util.*

class EventFragment : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private lateinit var auth: FirebaseAuth

    // event name, organization name, date, start time, end time, location, restrictions, description
    var eventName: String? = null
    var orgName: String? = null
    var date: String? = null
    var startTime: String? = null
    var endTime: String? = null
    var rawStartTime: String? = null
    var rawEndTime: String? = null
    var location: String? = null
    var position: Int = 0
    var descr: String? = null
    //    var restrictions: MutableList<String>? = null
    var imageID: String? = null
//    var event: Event? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Sets up binding.
        binding = FragmentEventBinding.inflate(inflater, container, false)
        var view = binding.root

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        eventName = this.arguments?.getString("eventName")
        orgName = this.arguments?.getString("orgName")
        date = this.arguments?.getString("date")
        startTime = this.arguments?.getString("startTime")
        endTime = this.arguments?.getString("endTime")
        rawStartTime = this.arguments?.getString("rawStartTime")
        rawEndTime = this.arguments?.getString("rawEndTime")
        location = this.arguments?.getString("location")
        position = this.requireArguments().getInt("position")
        descr = this.arguments?.getString("description")
//        restrictions = this.arguments?.get("restrictions") as MutableList<String>
        imageID = this.requireArguments().getString("image")
//        event = this.arguments?.get("event") as Event

        binding.eventNameText.text = eventName
        if (orgName == null) {
            binding.organizationNameText.visibility = View.GONE
        }
        else {
            binding.organizationNameText.text = orgName
        }
        binding.dateText.text = date
        binding.timeText.text = startTime + " - " + endTime
        binding.locationText.text = location
        binding.descriptionParagraphText.text = descr
        Picasso.with(view!!.context).load(imageID).placeholder(requireContext().getResources().getDrawable(R.drawable.noimage)).error(requireContext().getResources().getDrawable(R.drawable.noimage)).into(binding.eventImage)
        //binding.eventImage.setImageResource(imageID)

//        var restrictionsString = "Only: "
////        Log.d("", "Out restrictions, string = " + restrictions.toString())
////        Log.d("", "Out restrictions, size = " + restrictions!!.size.toString())
//        if(restrictions != null){
////            Log.d("", "In restrictions")
//            for (i in 0 until restrictions!!.size) {
//                restrictionsString += restrictions!!.get(i)
//                if(i != restrictions!!.size - 1){
//                    restrictionsString += ", "
//                }
//            }
//            binding.restrictionsText.text = restrictionsString
//        }
//        if(restrictionsString.equals("Only: ")){
//            binding.restrictionsText.text = "No Restrictions"
//        }

        binding.navigation.homeBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_eventFragment_to_forYouFragment)
        }
        binding.navigation.searchBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_eventFragment_to_searchFragment)
        }
        binding.navigation.settingsBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_eventFragment_to_settingsFragment)
        }
        if (user != null) {
            binding.addToCalendarButton.setOnClickListener {
                addToCalendar(user)
            }
        }
        else {
            binding.addToCalendarButton.isEnabled = false
            binding.addToCalendarButton.backgroundTintList = requireContext().resources.getColorStateList(R.color.lighterGray)
        }


        return view
    }

    private fun addToCalendar(user: FirebaseUser) {
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(CalendarScopes.CALENDAR)
        ).setSelectedAccountName(user.email)

        val calendar = Calendar.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).build()

        Thread {
            val event = Event().apply {
                summary = eventName
                description = descr
                start = EventDateTime().apply {
                    dateTime = DateTime(rawStartTime.toString())
                    timeZone = "America/New_York"
                }
                end = EventDateTime().apply {
                    dateTime = DateTime(rawEndTime.toString())
                    timeZone = "America/New_York"
                }
            }
            try {
                calendar.events().insert("primary", event).execute()
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(activity, "Event added.", Toast.LENGTH_SHORT).show()
                }
            }
            catch (e: Exception) {
                if (e is UserRecoverableAuthIOException) {
                    requestPermissions()
                }
                else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(activity, "Event can't be added at this time.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.start()
    }

    private fun requestPermissions() {
        GoogleSignIn.requestPermissions(
            requireActivity(),
            0,
            GoogleSignIn.getLastSignedInAccount(activity as Context),
            Scope(CalendarScopes.CALENDAR)
        )
    }
}
