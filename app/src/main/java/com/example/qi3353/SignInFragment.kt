package com.example.qi3353

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import com.example.qi3353.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var view: View
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: FragmentSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        var signInButton = binding.signInWithGoogle

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(view.context, gso)

        signInButton.setOnClickListener{
            signInGoogle()
        }

        // TEMPORARY!
        binding.signInWithGoogle.setOnClickListener{
            view.findNavController().navigate(R.id.action_signInFragment_to_preferencesFragment)
        }

        // place holder for now (need to change later on)
        binding.continueAsGuest.setOnClickListener{
            view.findNavController().navigate(R.id.action_signInFragment_to_forYouFragment)
        }

        return view
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        Log.e("error", "test1")
        launcher.launch(signInIntent)
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }else{
                Log.e("error", result.resultCode.toString())
                Activity.RESULT_CANCELED
            }

    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            Log.e("error", "test2")
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }
        else{
            Log.e("error", "test3")
            Toast.makeText(activity, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val intent : Intent = Intent(activity, SignInFragment::class.java)
                intent.putExtra("email", account.email)
                intent.putExtra("displayName", account.displayName)
                startActivity(intent)
                view.findNavController().navigate(R.id.action_signInFragment_to_preferencesFragment)
            }else{
                Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


}