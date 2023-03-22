package com.example.qi3353

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

const val REQUEST_CODE = 0

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var view: View
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        view = binding.root

        auth = FirebaseAuth.getInstance()

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        client = GoogleSignIn.getClient(view.context, options)

        binding.signInWithGoogle.setOnClickListener {
            signInWithGoogle()
        }

        binding.continueAsGuest.setOnClickListener {
            view.findNavController().navigate(R.id.action_signInFragment_to_forYouFragment)
        }

        return view
    }

    private fun signInWithGoogle() {
        val signInIntent = client.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                val account = task.result
                if (account != null) {
                    updateUI(account)
                }
                Log.d("ERROR (SignInFragment.kt): ",
                    "onActivityResult() => No Account")
            }
            else {
                Log.d("ERROR (SignInFragment.kt): ",
                    "onActivityResult() => " + task.exception.toString())
            }
        }
        else {
            Log.d("ERROR (SignInFragment.kt): ",
                "onActivityResult() => Result Code (" + resultCode + ")")
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(activity, "Sign in successful.", Toast.LENGTH_SHORT).show()
                view.findNavController().navigate(R.id.action_signInFragment_to_preferencesFragment)
            }
            else {
                Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}