package com.example.qi3353

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qi3353.databinding.FragmentForYouBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

class ForYouFragment : Fragment() {
    lateinit var database: FirebaseDatabase

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentForYouBinding

    private lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    //var eventList: MutableList<Event> = mutableListOf()

    private lateinit var liveEventList: MutableList<Event>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Sets up binding.
        binding = FragmentForYouBinding.inflate(inflater, container, false)
        var view = binding.root
        val model = ViewModelProvider(requireActivity()).get(EventViewModel::class.java)



        val prevFragId = findNavController().previousBackStackEntry!!.destination.id

        if ((prevFragId == R.id.signInFragment) || (prevFragId == R.id.preferencesFragment)) {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                // LEAVE EMPTY
            }
        }

        model.fetchEventList()
        model.eventListLiveData.observe(viewLifecycleOwner){ eventList ->
            liveEventList = eventList
            recyclerView = view.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            if(user != null){
                val preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                var temp: MutableSet<String> = mutableSetOf<String>()
                var clickedPref: Set<String> = preferences.getStringSet(user.email, temp) as Set<String>

//                Log.d("pref:", clickedPref.toString())
                // sort event list with events that have a matching tag higher

                var eventListFrequency: MutableList<String> = mutableListOf<String>()

                for(event in eventList) {
                    eventListFrequency.add(event.eventId)
                }


                for(tag in clickedPref) {
//                    Log.d("tag: ", tag.lowercase())
                    for(event in eventList){
                        val eventTags = event.tags
                        for(eventTag in eventTags){
                            if(tag.lowercase().equals(eventTag.lowercase())){
//                                Log.d("tag equals event tag: ", event.eventId + ", name: " + event.name)
                                eventListFrequency.add(event.eventId)
                                break
                            }
                        }
                    }
                }

                val comparator = compareByDescending<Map.Entry<String, Int>> { it.value }
                    .thenBy { it.key }
                var eventIDSorted = eventListFrequency.groupingBy { it }.eachCount().entries
                    .sortedWith(comparator).map { it.key }

//                if(!eventIDSorted.isEmpty()){
//                    Log.d("eventIDSorted: ", eventIDSorted[0])
//                }


                var eventListSorted: MutableList<Event> = mutableListOf<Event>()
                for(id in eventIDSorted){
                    for(event in eventList){
                        if(event.eventId.equals(id)){
                            eventListSorted.add(event)
                            break
                        }
                    }
                }

                // no preferences
                if(clickedPref.isEmpty()){
                    eventListSorted.sortBy { it.startRaw }
                }

                viewAdapter = RecyclerViewAdapter(eventListSorted.size, eventListSorted)
//                if(!eventListSorted.isEmpty()){
//                    Log.d("first event:", eventListSorted[0].name + ", id: " + eventListSorted[0].eventId)
//                }
                binding.pageTitle.title.text = "For You"
            }
            else {
                binding.pageTitle.title.text = "Home"
                eventList.sortBy { it.startRaw }

                viewAdapter = RecyclerViewAdapter(eventList.size, eventList)
            }
            recyclerView.adapter = viewAdapter

        }

        // Bottom navigation buttons.
        binding.navigation.searchBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_forYouFragment_to_searchFragment)
        }
        binding.navigation.settingsBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_forYouFragment_to_settingsFragment)
        }

        //eventList.clear()

        val jsonData = view.resources.openRawResource(
            view.resources.getIdentifier(
                "dummydata",
                "raw",
                requireActivity().packageName
            )
        ).bufferedReader().use { it.readText() }
        val outputJsonString = JSONObject(jsonData)


        val events = outputJsonString.getJSONArray("events") as JSONArray
        for (i in 0 until events.length()) {
//            val restrictionsList = mutableListOf<String>()

//            var jsonArray = events.getJSONObject(i).getJSONArray("restrictions")
//            Log.d("", "Strings: " + jsonArray.toString())
//            if (jsonArray != null) {
//                for (i in 0 until jsonArray.length()) {
////                    Log.d("", "adding: " + jsonArray[i].toString())
//                    restrictionsList.add(jsonArray[i].toString())
//                }
//            }

            val tagsList = mutableListOf<String>()

            var jsonArray = events.getJSONObject(i).getJSONArray("tags")
            if (jsonArray != null) {
                for (i in 0 until jsonArray.length()) {
                    tagsList.add(jsonArray[i].toString())
                }
            }

//            Log.d("", "tag list: " + tagsList.toString())
//            eventList.add(
//                Event(
//                    events.getJSONObject(i).get("eventId").toString(),
//                    events.getJSONObject(i).get("organization").toString(),
//                    events.getJSONObject(i).get("name").toString(),
//                    events.getJSONObject(i).get("description").toString(),
////                    restrictionsList,
//                    events.getJSONObject(i).get("start_time").toString(),
//                    events.getJSONObject(i).get("end_time").toString(),
//                    events.getJSONObject(i).get("location").toString(),
//                    tagsList,
//                    events.getJSONObject(i).get("photo").toString(),
//                    events.getJSONObject(i).get("date").toString()
//                )
//            )

//            restrictionsList.clear()


//            val eventId = events.getJSONObject(i).get("eventId")
//            val organization = events.getJSONObject(i).get("organization")
//            val name = events.getJSONObject(i).get("name")
//            val description = events.getJSONObject(i).get("description")
////            val restrictions = events.getJSONObject(i).get("restrictions") as MutableList<*>
//            val start_time = events.getJSONObject(i).get("start_time")
//            val end_time = events.getJSONObject(i).get("end_time")
//            val location = events.getJSONObject(i).get("location")
//            //val calendar_link = events.getJSONObject(i).get("calendar_link")
//            //val tags = events.getJSONObject(i).get("tags")
//            //val passed = events.getJSONObject(i).get("passed")
//            val photo = events.getJSONObject(i).get("photo")
//            //val original_link = events.getJSONObject(i).get("original_link")
//            val date = events.getJSONObject(i).get("date")

//            Log.d("test", eventId.toString())
//            Log.d("test", organization.toString())
//            Log.d("test", name.toString())
//            Log.d("test", description.toString())
////            Log.d("test", restrictions.toString())
//            Log.d("test", start_time.toString())
//            Log.d("test", end_time.toString())
//            Log.d("test", location.toString())
////            Log.d("test", calendar_link.toString())
////            Log.d("test", tags.toString())
////            Log.d("test", passed.toString())
//            Log.d("test", photo.toString())
////            Log.d("test", original_link.toString())
        }

        //Log.d("test", ""+outputJsonString)


        //recyclerView = view.findViewById(R.id.recyclerView)
        //recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)



        var clicked = 0;

        binding.hideHistorical.setOnClickListener {
            var eventListFrequency: MutableList<String> = mutableListOf<String>()

            for(event in liveEventList) {
                eventListFrequency.add(event.eventId)
            }
            val comparator = compareByDescending<Map.Entry<String, Int>> { it.value }
                .thenBy { it.key }
            var eventIDSorted = eventListFrequency.groupingBy { it }.eachCount().entries
                .sortedWith(comparator).map { it.key }


            if (clicked == 0) {
                var eventListSorted: MutableList<Event> = mutableListOf<Event>()
                for(id in eventIDSorted){
                    for(event in liveEventList){
                        if(event.eventId.equals(id) && LocalDate.parse(event.startRaw.substring(0, 10)) > LocalDate.now()){
                            eventListSorted.add(event)
                            break
                        }
                    }
                }
                eventListSorted.sortBy { it.startRaw }

                viewAdapter = RecyclerViewAdapter(eventListSorted.size, eventListSorted)
                recyclerView.adapter = viewAdapter
                clicked = 1;
                binding.hideHistorical.text = "Show All Events"
            }
            else if (clicked == 1) {
                var eventListSorted: MutableList<Event> = mutableListOf<Event>()
                for(id in eventIDSorted){
                    for(event in liveEventList){
                        if(event.eventId.equals(id)){
                            eventListSorted.add(event)
                            break
                        }
                    }
                }
                eventListSorted.sortBy { it.startRaw }
                viewAdapter = RecyclerViewAdapter(eventListSorted.size, eventListSorted)
                recyclerView.adapter = viewAdapter
                clicked = 0;
                binding.hideHistorical.text = "Hide Past Events"
            }
        }

        return view
    }


    inner class RecyclerViewAdapter(private var cnt: Int, private var events: MutableList<Event>) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        //private var classesLocal : MutableList<Classroom> = mutableListOf()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerViewAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            /* if (events[position].name.length > 20) {
                 holder.view.findViewById<TextView>(R.id.eventName).text = events[position].name.substring(0,17) + "..."
             }
             else {
             }
             */
            if (events[position].name.length > 20){
                holder.view.findViewById<TextView>(R.id.eventName).text = events[position].name.substring(0, 15) + "..."
            }
            else {
                holder.view.findViewById<TextView>(R.id.eventName).text = events[position].name
            }
            holder.view.findViewById<TextView>(R.id.date).text = events[position].date
            holder.view.findViewById<TextView>(R.id.time).text =
                events[position].start_time + " - " + events[position].end_time
            holder.view.findViewById<TextView>(R.id.location).text = events[position].location
            if (events[position].location.length > 30) {
                holder.view.findViewById<TextView>(R.id.location).text =
                    events[position].location.substring(0, 27) + "..."
            } else {
                holder.view.findViewById<TextView>(R.id.location).text = events[position].location
            }

            var stringGenerated = events[position].photo
            var thisimg = holder.view.findViewById<ImageView>(R.id.imageView)
            Picasso.with(view!!.context).load(stringGenerated).placeholder(context!!.getResources().getDrawable(R.drawable.noimage)).error(context!!.getResources().getDrawable(R.drawable.noimage)).into(thisimg)
//            val imgId = context!!.resources.getIdentifier(
//                "$stringGenerated",
//                "drawable",
//                context!!.packageName
//            )
//            holder.view.findViewById<ImageView>(R.id.imageView)
//                .setImageResource(imgId) //= events[position].photo

            //val calendar = context!!.resources.getIdentifier("calendar", "drawable", context!!.packageName)
            //holder.view.findViewById<ImageView>(R.id.calendarButton).setImageResource(calendar) //= events[position].photo


            holder.itemView.setOnClickListener {
                // interact with the item
//                Log.d("", "on click: " + events[position].restrictions.toString())

                NavHostFragment.findNavController(this@ForYouFragment).navigate(
                    R.id.action_forYouFragment_to_eventFragment,
                    bundleOf(
                        "eventName" to events[position].name,
                        "orgName" to events[position].organization,
                        "date" to events[position].date,
                        "startTime" to events[position].start_time,
                        "endTime" to events[position].end_time,
                        "location" to events[position].location,
                        "position" to position,
                        "description" to events[position].description,
                        "image" to stringGenerated,
                        "rawStartTime" to events[position].startRaw,
                        "rawEndTime" to events[position].endRaw
                    )
                )

//                bundleOf("eventName" to events[position].name, "date" to events[position].date,
//                    "startTime" to events[position].start_time, "endTime" to events[position].end_time,
//                    "location" to events[position].location, "position" to position, "event" to events[position]
//                )

                // event name, date, start time, end time, location, restrictions, description

            }
        }
        /*
            fun filterClasses(markerChoice: String, timeStart: String, timeEnd: String, date: String, part: String) {

                classes = classes.filter{ m -> m.building.equals(markerChoice, ignoreCase = true)}.toMutableList()
                classes = classes.filter{ m -> m.space >= part.toInt()}.toMutableList()

                val inFormat = SimpleDateFormat("MM/dd/yyyy")
                val myDate: Date = inFormat.parse(date)
                val simpleDateFormat = SimpleDateFormat("EEEE")
                val dayName: String = simpleDateFormat.format(myDate)

                classes = classes.filter{ m -> m.daysOfTheWeek.toString().contains(dayName, ignoreCase = true)}.toMutableList()

                classes = classes.filter{ m-> m.timeStart.replace(":", "").toInt() <= timeStart.replace(":", "").toInt() }.toMutableList()
                classes = classes.filter{ m-> m.timeEnd.replace(":", "").toInt() >= timeEnd.replace(":", "").toInt() }.toMutableList()

                cnt = classes.size
                notifyDataSetChanged()

            }
         */

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = cnt

        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            var eventName: TextView
            var imageView: ImageView
            var timeText: TextView
            var date: TextView
            var location: TextView
            //var calendar: ImageView

            init {
                eventName = view.findViewById(R.id.eventName)
                imageView = view.findViewById(R.id.imageView)
                timeText = view.findViewById(R.id.time)
                date = view.findViewById(R.id.date)
                location = view.findViewById(R.id.location)
                //calendar = view.findViewById(R.id.calendarButton)
            }
        }
    }


}