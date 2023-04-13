package com.example.qi3353

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EventRepository {
    var eventList: MutableList<Event> = mutableListOf()
    private val database = Firebase.database
    val myRef = database.reference.child("Events")

    fun fetchEventList(liveData: MutableLiveData<MutableList<Event>>) {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                eventList = mutableListOf<Event>()

                for (elem in dataSnapshot.children) {
                    val tagsList = mutableListOf<String>()

                    var jsonArray = JSONObject(elem.getValue(String::class.java)!!).getJSONArray("tags")
                    if (jsonArray != null) {
                        for (i in 0 until jsonArray.length()) {
                            if (jsonArray[i].toString() != "null") {
                                tagsList.add(jsonArray[i].toString())
                                //Log.d("test",jsonArray[i].toString())
                            }
                        }
                    }
                    var start = Integer.parseInt(JSONObject(elem.getValue(String::class.java)!!).get("start_time")
                        .toString().substring(0, 2));
                    var startTime : String = ""

                    if (start == 12){
                        startTime = start.toString() + JSONObject(elem.getValue(String::class.java)!!).get("start_time")
                            .toString().substring(2) + " pm"
                    }
                    else if (start == 0) {
                        startTime = "12" + JSONObject(elem.getValue(String::class.java)!!).get("start_time")
                            .toString().substring(2) + " am"
                    }
                    else if (start > 12) {
                        startTime = (start % 12).toString() + JSONObject(elem.getValue(String::class.java)!!).get("start_time")
                            .toString().substring(2) + " pm"
                    }
                    else if (start < 12){
                        startTime = start.toString() + JSONObject(elem.getValue(String::class.java)!!).get("start_time")
                            .toString().substring(2) + " am"
                    }

                    var end = Integer.parseInt(JSONObject(elem.getValue(String::class.java)!!).get("end_time")
                        .toString().substring(0, 2));
                    var endTime : String = ""

                    if (end == 12){
                        endTime = end.toString() + JSONObject(elem.getValue(String::class.java)!!).get("end_time")
                            .toString().substring(2) + " pm"
                    }
                    else if (end == 0) {
                        endTime = "12" + JSONObject(elem.getValue(String::class.java)!!).get("end_time")
                            .toString().substring(2) + " am"
                    }
                    else if (end > 12) {
                        endTime = (end % 12).toString() + JSONObject(elem.getValue(String::class.java)!!).get("end_time")
                            .toString().substring(2) + " pm"
                    }
                    else if (end < 12){
                        endTime = end.toString() + JSONObject(elem.getValue(String::class.java)!!).get("end_time")
                            .toString().substring(2) + " am"
                    }

                    var formatter = DateTimeFormatter.ofPattern("MMM dd, YYYY")
                    var date = LocalDate.parse(JSONObject(elem.getValue(String::class.java)!!).get("date")
                        .toString().substring(0, 10))
                    var formattedDate = date.format(formatter)

                    var formatter2 = DateTimeFormatter.ofPattern("MMM dd, YYYY")
                    var date2 = LocalDate.parse(JSONObject(elem.getValue(String::class.java)!!).get("date")
                        .toString().substring(13))
                    var formattedDate2 = date2.format(formatter2)



                    eventList.add(
                        Event(
                            JSONObject(elem.getValue(String::class.java)!!).get("eventId").toString(),
                            JSONObject(elem.getValue(String::class.java)!!).get("organization")
                                .toString(),
                            JSONObject(elem.getValue(String::class.java)!!).get("name").toString(),
                            JSONObject(elem.getValue(String::class.java)!!).get("description")
                                .toString(),
                            startTime,
                            endTime,
                            JSONObject(elem.getValue(String::class.java)!!).get("location").toString(),
                            tagsList,
                            JSONObject(elem.getValue(String::class.java)!!).get("photo").toString(),
                            formattedDate + " - " + formattedDate2,
                            JSONObject(elem.getValue(String::class.java)!!).get("startRaw").toString(),
                            JSONObject(elem.getValue(String::class.java)!!).get("endRaw").toString()
                        )
                    )
                }
                liveData.postValue(eventList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("test", "error")
            }
        })
    }
}