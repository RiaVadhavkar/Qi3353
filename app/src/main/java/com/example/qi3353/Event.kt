package com.example.qi3353

data class Event(
    var eventId: String,
    //var organization: String,
    var name: String,
    var description: String,
    var restrictions:  MutableList<String>,
    var start_time: String,
    var end_time: String,
    var location: String,
    //var calendar_link: MutableList<String>,
    var tags:  MutableList<String>,
    //var passed: Boolean,
    var photo: String,
    //var original_link: String
    var date: String
    )