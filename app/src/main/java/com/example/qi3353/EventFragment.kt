package com.example.qi3353

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentEventBinding
import com.example.qi3353.databinding.FragmentForYouBinding
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentEventBinding

    var eventName:String? = null
    var date:String? = null
    var startTime: String? = null
    var endTime:String? = null
    var location: String? = null
    var position: Int = 0
    var description: String? = null
    var restrictions: MutableList<String>? = null
    var imageID: Int = 0
//    var event: Event? = null
    // event name, date, start time, end time, location, restrictions, description

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Sets up binding.
        binding = FragmentEventBinding.inflate(inflater, container, false)
        var view = binding.root

        // Bottom navigation buttons.
        binding.navigation.homeBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_eventFragment_to_forYouFragment)
        }
        binding.navigation.searchBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_eventFragment_to_searchFragment)
        }
        binding.navigation.settingsBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_eventFragment_to_settingsFragment)
        }

        eventName = this.arguments?.getString("eventName")
        date =  this.arguments?.getString("date")
        startTime = this.arguments?.getString("startTime")
        endTime = this.arguments?.getString("endTime")
        location = this.arguments?.getString("location")
        position = this.requireArguments().getInt("position")
        description = this.arguments?.getString("description")
        restrictions = this.arguments?.get("restrictions") as MutableList<String>
        imageID = this.requireArguments().getInt("image")
//        event = this.arguments?.get("event") as Event

        binding.eventNameText.text = eventName
        binding.dateText.text = date
        binding.timeText.text = startTime + " - " + endTime
        binding.locationText.text = location
        binding.descriptionParagraphText.text = description
        binding.eventImage.setImageResource(imageID)

        var restrictionsString = "Only: "
//        Log.d("", "Out restrictions, string = " + restrictions.toString())
//        Log.d("", "Out restrictions, size = " + restrictions!!.size.toString())
        if(restrictions != null){
//            Log.d("", "In restrictions")
            for (i in 0 until restrictions!!.size) {
                restrictionsString += restrictions!!.get(i)
                if(i != restrictions!!.size - 1){
                    restrictionsString += ", "
                }
            }
            binding.restrictionsText.text = restrictionsString
        }
        if(restrictionsString.equals("Only: ")){
            binding.restrictionsText.text = "No Restrictions"
        }



        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EventFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EventFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}