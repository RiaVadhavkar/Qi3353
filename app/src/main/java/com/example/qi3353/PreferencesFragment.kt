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
        // Sets up binding.
        binding = FragmentPreferencesBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser



        // Sets up recycler view.
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
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
                            binding.tag.setBackgroundColor(Color.parseColor("#A0A0A0"))
                            clickedPref.add(tag.lowercase())
                            break
                        }
                    }
                }

                binding.tag.text = tag
                binding.tag.setOnClickListener {
                    clicked = !clicked
                    if (clicked) {
                        binding.tag.setBackgroundColor(Color.parseColor("#A0A0A0"))
                        clickedPref.add(tag.lowercase())
                    }
                    else {
                        binding.tag.setBackgroundColor(Color.parseColor("#E0E0E0"))
                        clickedPref.remove(tag.lowercase())
                    }
                }
            }
        }
    }
}