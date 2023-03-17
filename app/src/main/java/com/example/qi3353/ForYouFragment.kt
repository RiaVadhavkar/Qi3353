package com.example.qi3353

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForYouFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForYouFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    var eventList :  MutableList<Event> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_for_you, container, false)



        val jsonData=view.resources.openRawResource(
            view.resources.getIdentifier(
                "dummydata",
                "raw",
                requireActivity().packageName
            )
        ).bufferedReader().use{it.readText()}
        val outputJsonString=JSONObject(jsonData)

        val events = outputJsonString.getJSONArray("events") as JSONArray
        for (i in 0 until events.length()){

            eventList.add(
                Event(
                    events.getJSONObject(i).get("eventId").toString(),
                    events.getJSONObject(i).get("name").toString(),
                    events.getJSONObject(i).get("start_time").toString(),
                    events.getJSONObject(i).get("end_time").toString(),
                    events.getJSONObject(i).get("location").toString(),
                    events.getJSONObject(i).get("photo").toString()
                )
            )



            val eventId = events.getJSONObject(i).get("eventId")
            //val organization = events.getJSONObject(i).get("organization")
            val name = events.getJSONObject(i).get("name")
            //val description = events.getJSONObject(i).get("description")
            //val restrictions = events.getJSONObject(i).get("restrictions")
            val start_time = events.getJSONObject(i).get("start_time")
            val end_time= events.getJSONObject(i).get("end_time")
            val location = events.getJSONObject(i).get("location")
            //val calendar_link = events.getJSONObject(i).get("calendar_link")
            //val tags = events.getJSONObject(i).get("tags")
            //val passed = events.getJSONObject(i).get("passed")
            val photo = events.getJSONObject(i).get("photo")
            //val original_link = events.getJSONObject(i).get("original_link")
/*
            Log.d("test", eventId.toString())
            Log.d("test", organization.toString())
            Log.d("test", name.toString())
            Log.d("test", description.toString())
            Log.d("test", restrictions.toString())
            Log.d("test", start_time.toString())
            Log.d("test", end_time.toString())
            Log.d("test", location.toString())
            Log.d("test", calendar_link.toString())
            Log.d("test", tags.toString())
            Log.d("test", passed.toString())
            Log.d("test", photo.toString())
            Log.d("test", original_link.toString())
*/
        }

        //Log.d("test", ""+outputJsonString)


        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewAdapter = RecyclerViewAdapter(10, eventList)
        recyclerView.adapter = viewAdapter


        return view
    }


    inner class RecyclerViewAdapter(private var cnt: Int, private var events: MutableList<Event> ) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        //private var classesLocal : MutableList<Classroom> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): RecyclerViewAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item, parent, false)
            return ViewHolder(v)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.view.findViewById<TextView>(R.id.eventName).text = events[position].name
            //holder.view.findViewById<TextView>(R.id.imageView).text = events[position].photo
            //holder.view.findViewById<TextView>(R.id.date).text =
            holder.view.findViewById<TextView>(R.id.time).text = events[position].start_time + " - " + events[position].end_time
            holder.view.findViewById<TextView>(R.id.location).text = events[position].location
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

        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
            var eventName: TextView
            //var imageView: TextView
            var timeText: TextView
            //var date: TextView
            var location: TextView

            init {
                eventName = view.findViewById(R.id.eventName)
                //timeText = view.findViewById(R.id.imageView)
                timeText = view.findViewById(R.id.time)
                //date = view.findViewById(R.id.date)
                location = view.findViewById(R.id.location)
            }
        }
    }



}