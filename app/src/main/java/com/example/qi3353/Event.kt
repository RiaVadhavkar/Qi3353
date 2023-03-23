package com.example.qi3353

data class Event(
    var eventId: String,
    //var organization: String, TODO WILL USE
    var name: String,
    var description: String,
    var restrictions:  MutableList<String>, // TODO DELETE
    var start_time: String,
    var end_time: String,
    var location: String,
    //var calendar_link: MutableList<String>, //TODO USE - GONNA TAKE A WHILE
    var tags:  MutableList<String>, //TODO use function from Search Fragment and implement create a String list
    //var passed: Boolean, //TODO see if start_date has passed or not
    var photo: String, //TODO append url in the database
    //var original_link: String //TODO
    var date: String
    )