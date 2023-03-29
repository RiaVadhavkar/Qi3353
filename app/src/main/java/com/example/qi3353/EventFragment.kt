package com.example.qi3353

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import  androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentEventBinding
import com.squareup.picasso.Picasso
import java.util.*

class EventFragment : Fragment() {

    private lateinit var binding: FragmentEventBinding

    // event name, organization name, date, start time, end time, location, restrictions, description
    var eventName: String? = null
    var orgName: String? = null
    var date: String? = null
    var startTime: String? = null
    var endTime: String? = null
    var location: String? = null
    var position: Int = 0
    var description: String? = null
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

        eventName = this.arguments?.getString("eventName")
        orgName = this.arguments?.getString("orgName")
        date = this.arguments?.getString("date")
        startTime = this.arguments?.getString("startTime")
        endTime = this.arguments?.getString("endTime")
        location = this.arguments?.getString("location")
        position = this.requireArguments().getInt("position")
        description = this.arguments?.getString("description")
//        restrictions = this.arguments?.get("restrictions") as MutableList<String>
        imageID = this.requireArguments().getString("image")
//        event = this.arguments?.get("event") as Event

        binding.eventNameText.text = eventName
        binding.organizationNameText.text = orgName
        binding.dateText.text = date
        binding.timeText.text = startTime + " - " + endTime
        binding.locationText.text = location
        binding.descriptionParagraphText.text = description
        Picasso.with(view!!.context).load(imageID).into(binding.eventImage)
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


        return view
    }
}
