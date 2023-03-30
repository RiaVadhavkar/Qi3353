package com.example.qi3353

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qi3353.databinding.FragmentForYouBinding
import com.example.qi3353.databinding.FragmentSearchResultBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Use the [SearchResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchResultFragment : Fragment() {


    private lateinit var binding: FragmentSearchResultBinding

    private lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>

    var eventFilter: MutableList<Event> = mutableListOf()
    var searchTerm: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Sets up binding.
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        var view = binding.root

        // bundle to get either the keyword or tag
        eventFilter = this.arguments?.get("filteredEvent") as MutableList<Event>
        searchTerm = this.requireArguments().getString("searchTerm").toString()

        binding.searchResultTitle.text = "Result(s) for: \"" + searchTerm.lowercase() + "\""


        // Bottom navigation buttons.
        binding.navigation.homeBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchResultFragment_to_forYouFragment)
        }
        binding.navigation.searchBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchResultFragment_to_searchFragment)
        }
        binding.navigation.settingsBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchResultFragment_to_settingsFragment)
        }



        recyclerView = view.findViewById(R.id.recyclerViewSearchResult)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewAdapter = RecyclerViewAdapter(eventFilter.size, eventFilter)
        recyclerView.adapter = viewAdapter


        return view
    }


    inner class RecyclerViewAdapter(private var cnt: Int, private var events: MutableList<Event>) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerViewAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (events[position].name.length > 20){
                holder.view.findViewById<TextView>(R.id.eventName).text = events[position].name.substring(0, 17) + "..."
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
            Picasso.with(view!!.context).load(stringGenerated).placeholder(requireContext().getResources().getDrawable(R.drawable.noimage)).error(requireContext().getResources().getDrawable(R.drawable.noimage)).into(thisimg)

            //val calendar = context!!.resources.getIdentifier("calendar", "drawable", context!!.packageName)
            //holder.view.findViewById<ImageView>(R.id.calendarButton).setImageResource(calendar) //= events[position].photo


            holder.itemView.setOnClickListener {
                // interact with the item
//                Log.d("", "on click: " + events[position].restrictions.toString())

                NavHostFragment.findNavController(this@SearchResultFragment).navigate(
                    R.id.action_searchResultFragment_to_eventFragment,
                    bundleOf(
                        "eventName" to events[position].name,
                        "date" to events[position].date,
                        "startTime" to events[position].start_time,
                        "endTime" to events[position].end_time,
                        "location" to events[position].location,
                        "position" to position,
                        "description" to events[position].description,
                        "image" to stringGenerated
                    )
                )

//                bundleOf("eventName" to events[position].name, "date" to events[position].date,
//                    "startTime" to events[position].start_time, "endTime" to events[position].end_time,
//                    "location" to events[position].location, "position" to position, "event" to events[position]
//                )

                // event name, date, start time, end time, location, restrictions, description

            }
        }

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