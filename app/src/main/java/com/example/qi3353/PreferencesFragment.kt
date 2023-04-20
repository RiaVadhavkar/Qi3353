package com.example.qi3353

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.qi3353.databinding.FragmentPreferencesBinding
import com.example.qi3353.databinding.TagPreferencesItemBinding
import com.google.firebase.auth.FirebaseAuth


class PreferencesFragment : Fragment() {
    private lateinit var binding: FragmentPreferencesBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private var clickedPref: MutableSet<String> = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreferencesBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser



        // Sets up recycler view.
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = StaggeredGridLayoutManager(13, StaggeredGridLayoutManager.HORIZONTAL)
        recyclerView.adapter = PreferencesRVA(activity as MainActivity)

        binding.continueButton.setOnClickListener {
            if(user != null) {
                val preferences = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                var editor = preferences.edit()

                // "key: email", "value : set of preferences"
                editor.putStringSet(user.email, clickedPref)
                editor.commit()
            }
            view.findNavController().navigate(R.id.action_preferencesFragment_to_forYouFragment)
        }

        return view
    }

    inner class PreferencesRVA(private val activity: MainActivity) :
        RecyclerView.Adapter<PreferencesRVA.ViewHolder>() {
        private lateinit var binding: TagPreferencesItemBinding

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): PreferencesRVA.ViewHolder {
            binding = TagPreferencesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val view = binding.root

            return ViewHolder(view, binding, activity)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(Tags.values()[position].toString())
        }

        override fun getItemCount() = Tags.values().size

        inner class ViewHolder(
            private val view: View,
            private val binding: TagPreferencesItemBinding,
            private val activity: MainActivity
        ) :
            RecyclerView.ViewHolder(view) {
            fun bindItems(tag: String) {
                var clicked = false

                auth = FirebaseAuth.getInstance()
                val user = auth.currentUser

                if(user != null) {
                    val preferences = view.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE)
                    var temp: MutableSet<String> = mutableSetOf<String>()
                    var preferencesList: Set<String> = preferences.getStringSet(user.email, temp) as Set<String>

                    for(pref in preferencesList){
                        if(pref.lowercase().equals(tag.lowercase())){
                            clicked = true
                            binding.tag.setBackgroundTintList(requireContext().resources.getColorStateList(R.color.lightYellow))
                            clickedPref.add(tag.lowercase())
                            break
                        }
                    }
                }

                binding.tag.text = when (tag.lowercase()) {
                    "diversity" -> getString(R.string.diversityEmoji) + " " + getString(R.string.diversity)
                    "cultural" -> getString(R.string.culturalEmoji) + " " + getString(R.string.cultural)
                    "international" -> getString(R.string.internationalEmoji) + " " + getString(R.string.international)
                    "religious" -> getString(R.string.religiousEmoji) + " " + getString(R.string.religious)
                    "gender" -> getString(R.string.genderEmoji) + " " + getString(R.string.gender)
                    "lgbtq" -> getString(R.string.lgbtqEmoji) + " " + getString(R.string.lgbtq)
                    "social" -> getString(R.string.socialEmoji) + " " + getString(R.string.social)
                    "food" -> getString(R.string.foodEmoji) + " " + getString(R.string.food)
                    "art" -> getString(R.string.artEmoji) + " " + getString(R.string.art)
                    "music" -> getString(R.string.musicEmoji) + " " + getString(R.string.music)
                    "dance" -> getString(R.string.danceEmoji) + " " + getString(R.string.dance)
                    "community" -> getString(R.string.communityEmoji) + " " + getString(R.string.community)
                    "volunteer" -> getString(R.string.volunteerEmoji) + " " + getString(R.string.volunteer)
                    "utprosim" -> getString(R.string.utprosimEmoji) + " " + getString(R.string.utprosim)
                    "academic" -> getString(R.string.academicEmoji) + " " + getString(R.string.academic)
                    "gaming" -> getString(R.string.gamingEmoji) + " " + getString(R.string.gaming)
                    "sports" -> getString(R.string.sportsEmoji) + " " + getString(R.string.sports)
                    "fitness" -> getString(R.string.fitnessEmoji) + " " + getString(R.string.fitness)
                    "health" -> getString(R.string.healthEmoji) + " " + getString(R.string.health)
                    "grads" -> getString(R.string.gradsEmoji) + " " + getString(R.string.grads)
                    "alumni" -> getString(R.string.alumniEmoji) + " " + getString(R.string.alumni)
                    "miscellaneous" -> getString(R.string.miscellaneousEmoji) + " " + getString(R.string.miscellaneous)
                    else -> ""
                }

                binding.tag.setOnClickListener {
                    clicked = !clicked
                    if (clicked) {
                        binding.tag.setBackgroundTintList(requireContext().resources.getColorStateList(R.color.lightYellow))
                        clickedPref.add(tag.lowercase())
                    }
                    else {
                        binding.tag.setBackgroundTintList(null)
                        clickedPref.remove(tag.lowercase())
                    }
                }
            }
        }
    }
}