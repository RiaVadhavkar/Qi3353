/*package com.example.qi3353

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class NotificationWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    private val workManager = WorkManager.getInstance(appContext)
    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {
        val appContext = applicationContext
        var title = ""
        var message = ""
        var isChanged = ""
        // Create the observer which updates the UI.
        //val nameObserver = Observer<MutableList<Event>> {
        //}

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        //model.eventListLiveData.observe(this, nameObserver)
        Log.e("test", "line 61")
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference()
        reference.child("Events")//"myTopic/topic-name")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val children = dataSnapshot.children

                    children.forEach {
                        //Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                        Log.e("test", "line 71")

                        val body = dataSnapshot.child("body").value.toString()
                        isChanged = JSONObject(it.getValue(String::class.java)!!).get("isNew").toString()
                        if (isChanged == "yes") {
                            //title = dataSnapshot.child("title").value.toString()
                            title =
                                JSONObject(it.getValue(String::class.java)!!).get("organization")
                                    .toString()
                            message = JSONObject(it.getValue(String::class.java)!!).get("name")
                                .toString() + " is hosting an event!"
                            isChanged = "no"
                        }
                    }
                    //val message = binding.etMessage.text.toString()
                    //demo key:
                    // fnhm0gU7S6m_NZOwueBrLP:APA91bGPBHmwZVU5SVavmWLRNKsE8tgVzOy4VTGVQPXru6k5VCQzUGojrYN-zmaM8Mzf2jTh2aP5oDkt-XpjQxSmsCRdL8uAMH8lOH4dhtVuS0rwe66h4BRqzyD_zdCtNGOPEKEQd6bq
                    //val recipientToken = binding.etToken.text.toString()
                    val recipientToken = "fnhm0gU7S6m_NZOwueBrLP:APA91bGPBHmwZVU5SVavmWLRNKsE8tgVzOy4VTGVQPXru6k5VCQzUGojrYN-zmaM8Mzf2jTh2aP5oDkt-XpjQxSmsCRdL8uAMH8lOH4dhtVuS0rwe66h4BRqzyD_zdCtNGOPEKEQd6bq"




                        Log.e("test", "line 88")
                    if (title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                        PushNotification(NotificationData(title, message), recipientToken).also {
                            sendNotification(it)
                            Log.e("test", "line 92")
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
            )
//        val json = JSONObject()
//        json.put("username", inputData.getString("username"))
//        json.put("event", inputData.getString("event"))
//        Log.d("PP05_Tag", "params:"+json.toString()+" url "+MainActivity.URL)
//        return uploadLog(json, MainActivity.URL)
        return Result.Success()
    }

//    fun uploadLog(json: JSONObject, url: String): Result {
//        Log.d("PP05_Tag", "uploadLog() "+url)
//        var call = TrackerRetrofitService.create(url).postLog(json)
//        call.execute()
//        return Result.success()
//    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch{
        try {
            val response = RetrofitInstance.api.postNotification(notification)

            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")


            }
            else {
                Log.e(TAG, response.errorBody().toString())
                Log.e("test", "test else statement")
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
            Log.e("test", "catch clause")
        }
    }

}*/