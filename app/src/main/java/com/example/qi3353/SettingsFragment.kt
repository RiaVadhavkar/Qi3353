package com.example.qi3353

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Switch
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentSettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)


        var view = binding.root

        var notificationSwitch = view.findViewById<Switch>(R.id.notificationSwitch)

        auth = FirebaseAuth.getInstance()

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        client = GoogleSignIn.getClient(view.context, options)

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
            auth.signOut()
            client.signOut()
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

        notificationSwitch.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked){
                view.findNavController().navigate(R.id.action_settingsFragment_to_notificationTokenFragment);
            }
        }

        return view
    }

    //notificationSwitch
}