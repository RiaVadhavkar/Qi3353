package com.example.qi3353

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

class EventViewModel : ViewModel() {
    var eventListLiveData = MutableLiveData<MutableList<Event>>()
    private var eventList: MutableList<Event> = mutableListOf()
    private val repository = EventRepository()

    init {
        eventListLiveData.value = eventList
    }

    fun fetchEventList() {
        repository.fetchEventList(eventListLiveData)
    }




}