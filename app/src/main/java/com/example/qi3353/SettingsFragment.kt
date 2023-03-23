package com.example.qi3353

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        var view = binding.root

        binding.navigation.homeBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_settingsFragment_to_forYouFragment)
        }
        binding.navigation.searchBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_settingsFragment_to_searchFragment)
        }
        binding.preferencesButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_settingsFragment_to_preferencesFragment)
        }
        binding.logoutButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_settingsFragment_to_signInFragment)
        }

        val spinner = binding.reminderSpinner
        ArrayAdapter.createFromResource(
            activity as Context,
            R.array.reminderTimes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }

        return view
    }
}