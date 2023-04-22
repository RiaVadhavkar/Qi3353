package com.example.qi3353

data class Event(
    var eventId: String,
    var organization: String,
    var name: String,
    var description: String,
    var start_time: String,
    var end_time: String,
    var location: String,
    var tags:  MutableList<String>,
    var photo: String,
    var date: String,
    var startRaw: String,
    var endRaw: String,
    var isNew: String,
    )