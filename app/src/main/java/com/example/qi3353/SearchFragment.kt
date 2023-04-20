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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
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
        val model = ViewModelProvider(requireActivity()).get(EventViewModel::class.java)

        binding.pageTitle.title.text = "Search"

        //eventList.clear()

        model.fetchEventList()
        model.eventListLiveData.observe(viewLifecycleOwner) { modelList ->
            eventList = modelList
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
        recyclerView.layoutManager = GridLayoutManager(context, 3)
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
                binding.emoji.text = when (tag.lowercase()) {
                    "diversity" -> getString(R.string.diversityEmoji)
                    "cultural" -> getString(R.string.culturalEmoji)
                    "international" -> getString(R.string.internationalEmoji)
                    "religious" -> getString(R.string.religiousEmoji)
                    "gender" -> getString(R.string.genderEmoji)
                    "lgbtq" -> getString(R.string.lgbtqEmoji)
                    "social" -> getString(R.string.socialEmoji)
                    "food" -> getString(R.string.foodEmoji)
                    "art" -> getString(R.string.artEmoji)
                    "music" -> getString(R.string.musicEmoji)
                    "dance" -> getString(R.string.danceEmoji)
                    "community" -> getString(R.string.communityEmoji)
                    "volunteer" -> getString(R.string.volunteerEmoji)
                    "utprosim" -> getString(R.string.utprosimEmoji)
                    "academic" -> getString(R.string.academicEmoji)
                    "gaming" -> getString(R.string.gamingEmoji)
                    "sports" -> getString(R.string.sportsEmoji)
                    "fitness" -> getString(R.string.fitnessEmoji)
                    "health" -> getString(R.string.healthEmoji)
                    "grads" -> getString(R.string.gradsEmoji)
                    "alumni" -> getString(R.string.alumniEmoji)
                    "miscellaneous" -> getString(R.string.miscellaneousEmoji)
                    else -> ""
                }
                binding.tag.text = when (tag.lowercase()) {
                    "diversity" -> getString(R.string.diversity)
                    "cultural" -> getString(R.string.cultural)
                    "international" -> getString(R.string.international)
                    "religious" -> getString(R.string.religious)
                    "gender" -> getString(R.string.gender)
                    "lgbtq" -> getString(R.string.lgbtq)
                    "social" -> getString(R.string.social)
                    "food" -> getString(R.string.food)
                    "art" -> getString(R.string.art)
                    "music" -> getString(R.string.music)
                    "dance" -> getString(R.string.dance)
                    "community" -> getString(R.string.community)
                    "volunteer" -> getString(R.string.volunteer)
                    "utprosim" -> getString(R.string.utprosim)
                    "academic" -> getString(R.string.academic)
                    "gaming" -> getString(R.string.gaming)
                    "sports" -> getString(R.string.sports)
                    "fitness" -> getString(R.string.fitness)
                    "health" -> getString(R.string.health)
                    "grads" -> getString(R.string.grads)
                    "alumni" -> getString(R.string.alumni)
                    "miscellaneous" -> getString(R.string.miscellaneous)
                    else -> ""
                }.uppercase()
            }
        }
    }
}