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

                    for (elm in elem.child("tags").children){
                        Log.d("hi", elm.value.toString())
                        tagsList.add(elm.value.toString())
                    }


//                    var jsonArray = JSONObject(elem.getValue(String::class.java)!!).getJSONArray("tags")
//                    if (jsonArray != null) {
//                        for (i in 0 until jsonArray.length()) {
//                            if (jsonArray[i].toString() != "null") {
//                                tagsList.add(jsonArray[i].toString())
//                                //Log.d("test",jsonArray[i].toString())
//                            }
//                        }
//                    }
                    var start = Integer.parseInt(elem.child("start_time").value
                        .toString().substring(0, 2));
                    var startTime : String = ""

                    if (start == 12){
                        startTime = start.toString() + elem.child("start_time").value
                            .toString().substring(2) + " pm"
                    }
                    else if (start == 0) {
                        startTime = "12" + elem.child("start_time").value
                            .toString().substring(2) + " am"
                    }
                    else if (start > 12) {
                        startTime = (start % 12).toString() + elem.child("start_time").value
                            .toString().substring(2) + " pm"
                    }
                    else if (start < 12){
                        startTime = start.toString() + elem.child("start_time").value
                            .toString().substring(2) + " am"
                    }

                    var end = Integer.parseInt(elem.child("end_time").value
                        .toString().substring(0, 2));
                    var endTime : String = ""

                    if (end == 12){
                        endTime = end.toString() + elem.child("end_time").value
                            .toString().substring(2) + " pm"
                    }
                    else if (end == 0) {
                        endTime = "12" + elem.child("end_time").value
                            .toString().substring(2) + " am"
                    }
                    else if (end > 12) {
                        endTime = (end % 12).toString() + elem.child("end_time").value
                            .toString().substring(2) + " pm"
                    }
                    else if (end < 12){
                        endTime = end.toString() + elem.child("end_time").value
                            .toString().substring(2) + " am"
                    }

                    var formatter = DateTimeFormatter.ofPattern("MMM dd, YYYY")
                    var date = LocalDate.parse(elem.child("date").value
                        .toString().substring(0, 10))
                    var formattedDate = date.format(formatter)

                    var formatter2 = DateTimeFormatter.ofPattern("MMM dd, YYYY")
                    var date2 = LocalDate.parse(elem.child("date").value
                        .toString().substring(13))
                    var formattedDate2 = date2.format(formatter2)



                    eventList.add(
                        Event(
                            elem.child("eventId").value.toString(),
                            elem.child("organization").value
                                .toString(),
                            elem.child("name").value.toString(),
                            elem.child("description").value
                                .toString(),
                            startTime,
                            endTime,
                            elem.child("location").value.toString(),
                            tagsList,
                            elem.child("photo").value.toString(),
                            formattedDate + " - " + formattedDate2,
                            elem.child("startRaw").value.toString(),
                            elem.child("endRaw").value.toString(),
                            elem.child("isNew").value.toString()
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