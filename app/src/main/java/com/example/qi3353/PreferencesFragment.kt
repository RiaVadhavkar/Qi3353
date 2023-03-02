package com.example.qi3353

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qi3353.databinding.FragmentPreferencesBinding
import com.example.qi3353.databinding.TagPreferencesItemBinding

class PreferencesFragment : Fragment() {
    private lateinit var binding: FragmentPreferencesBinding
    private val viewModel: Model by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Sets up binding.
        binding = FragmentPreferencesBinding.inflate(inflater, container,false)
        val view = binding.root

        // Sets up recycler view.
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        viewModel.getTags().observe(viewLifecycleOwner) {
            recyclerView.adapter = PreferencesRVA(it, activity as MainActivity)
        }

        binding.continueBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_preferencesFragment_to_forYouFragment)
        }

        return view
    }
}

class PreferencesRVA(private val tags: List<String>, private val activity: MainActivity) :
    RecyclerView.Adapter<PreferencesRVA.ViewHolder>() {
    private lateinit var binding: TagPreferencesItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferencesRVA.ViewHolder {
        binding = TagPreferencesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        return ViewHolder(view, binding, activity)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(tags[position])
    }

    override fun getItemCount() = tags.size

    class ViewHolder(private val view: View, private val binding: TagPreferencesItemBinding, private val activity: MainActivity) :
        RecyclerView.ViewHolder(view) {
            fun bindItems(tag: String) {
                var clicked = false

                binding.tag.text = tag
                binding.tag.setOnClickListener {
                    clicked = !clicked
                    if (clicked) {
                        binding.tag.setBackgroundColor(Color.parseColor("#A0A0A0"))
                    }
                    else {
                        binding.tag.setBackgroundColor(Color.parseColor("#E0E0E0"))
                    }
                }
            }
    }
}