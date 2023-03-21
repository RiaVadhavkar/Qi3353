package com.example.qi3353

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qi3353.databinding.FragmentSearchBinding
import com.example.qi3353.databinding.TagSearchItemBinding

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerView: RecyclerView

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

        // Sets up recycler view.
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = SearchRVA(activity as MainActivity)

        // Bottom navigation buttons.
        binding.navigation.homeBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchFragment_to_forYouFragment)
        }
        binding.navigation.profileBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchFragment_to_profileFragment)
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