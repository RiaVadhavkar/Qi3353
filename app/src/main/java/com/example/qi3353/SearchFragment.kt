package com.example.qi3353

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qi3353.databinding.FragmentSearchBinding
import com.example.qi3353.databinding.TagSearchItemBinding
import org.json.JSONArray
import org.json.JSONObject

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerView: RecyclerView

    var eventList :  MutableList<Event> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Sets up binding.
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        eventList.clear()

        // getting data from json
        val jsonData=view.resources.openRawResource(
            view.resources.getIdentifier(
                "dummydata",
                "raw",
                requireActivity().packageName
            )
        ).bufferedReader().use{it.readText()}
        val outputJsonString= JSONObject(jsonData)


        val events = outputJsonString.getJSONArray("events") as JSONArray
        for (i in 0 until events.length()){
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
            eventList.add(
                Event(
                    events.getJSONObject(i).get("eventId").toString(),
                    events.getJSONObject(i).get("organization").toString(),
                    events.getJSONObject(i).get("name").toString(),
                    events.getJSONObject(i).get("description").toString(),
//                    restrictionsList,
                    events.getJSONObject(i).get("start_time").toString(),
                    events.getJSONObject(i).get("end_time").toString(),
                    events.getJSONObject(i).get("location").toString(),
                    tagsList,
                    events.getJSONObject(i).get("photo").toString(),
                    events.getJSONObject(i).get("date").toString()
                )
            )

        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(msg: String): Boolean {
                var filteredlist: MutableList<Event> = mutableListOf()

                if(msg.equals("")){
                    filteredlist = eventList
                }
                else {
                    for (item in eventList) {
                        // checking if the entered string matched with any event
                        // checks event name
                        if (item.name.lowercase().contains(msg.lowercase())) {
                            filteredlist.add(item)
                            continue
                        }
                        // checks event tags
                        val eventTags = item.tags
                        for(tag in eventTags){
                            if(msg.lowercase().equals(tag.lowercase())){
                                filteredlist.add(item)
                                continue
                            }
                        }
                        // checks description
                        if(item.description.lowercase().contains(msg.lowercase())){
                            filteredlist.add(item)
                            continue
                        }
                    }
                }

                // navigate to searchResult page and send with filteredlist
                NavHostFragment.findNavController(this@SearchFragment).navigate(R.id.action_searchFragment_to_searchResultFragment,
                    bundleOf("filteredEvent" to filteredlist, "searchTerm" to msg)
                )

                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
//                Log.d(TAG, msg)
                return false
            }
        })

        // Sets up recycler view.
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = SearchRVA(activity as MainActivity)

        // Bottom navigation buttons.
        binding.navigation.homeBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchFragment_to_forYouFragment)
        }
        binding.navigation.settingsBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchFragment_to_settingsFragment)
        }

        return view
    }



    inner class SearchRVA(private val activity: MainActivity) :
        RecyclerView.Adapter<SearchRVA.ViewHolder>() {
        private lateinit var binding: TagSearchItemBinding

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRVA.ViewHolder {
            binding =
                TagSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val view = binding.root
            return ViewHolder(view, binding, activity)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(Tags.values()[position].toString())

            holder.itemView.setOnClickListener {
                val tagName = Tags.values()[position].toString()
                var filteredlist: MutableList<Event> = mutableListOf()

                for (item in eventList) {
                    // add to list if the event contains tagName
                    val eventTags = item.tags
                    for(tag in eventTags){
                        if(tagName.lowercase().equals(tag.lowercase())){
                            filteredlist.add(item)
                            continue
                        }
                    }
                }

                // navigate to searchResult page and send with filteredlist
                NavHostFragment.findNavController(this@SearchFragment).navigate(R.id.action_searchFragment_to_searchResultFragment,
                    bundleOf("filteredEvent" to filteredlist, "searchTerm" to tagName)
                )

            }
        }

        override fun getItemCount() = Tags.values().size

        inner class ViewHolder(
            private val view: View,
            private val binding: TagSearchItemBinding,
            private val activity: MainActivity
        ) :
            RecyclerView.ViewHolder(view) {
            fun bindItems(tag: String) {
                binding.tag.text = tag
            }
        }
    }
}