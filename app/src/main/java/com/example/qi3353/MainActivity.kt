package com.example.qi3353

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewTreeObserver
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.view.View
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
        }

        var keepSplashOnScreen = true
        val delay = 2000L

        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        Handler(Looper.getMainLooper()).postDelayed({ keepSplashOnScreen = false }, delay)

        //val requester = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 1, TimeUnit.DAYS).addTag("thisWorker").build()

        //WorkManager.getInstance().enqueueUniquePeriodicWork("thisWorker",ExistingPeriodicWorkPolicy.REPLACE, requester)
        setContentView(R.layout.activity_main)
    }
}