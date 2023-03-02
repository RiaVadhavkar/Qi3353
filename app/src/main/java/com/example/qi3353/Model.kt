package com.example.qi3353

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Model : ViewModel() {
    private val tags = MutableLiveData<List<String>>()
    private val tagsLocal = ArrayList<String>()

    init {
        initTags(tagsLocal)
        tags.postValue(tagsLocal)
    }

    fun getTags(): LiveData<List<String>> {
        return tags
    }

    private fun initTags(tags: ArrayList<String>) {
        tags.clear()
        tags.add("Diversity")
        tags.add("Cultural")
        tags.add("International")
        tags.add("Religious")
        tags.add("Gender")
        tags.add("LGBTQ+")
        tags.add("Social")
        tags.add("Food & Dining")
        tags.add("Art")
        tags.add("Music")
        tags.add("Dance")
        tags.add("Community")
        tags.add("Volunteer")
        tags.add("Ut Prosim")
        tags.add("Academic")
        tags.add("Chess")
        tags.add("Gaming")
        tags.add("Sports")
        tags.add("Fitness")
        tags.add("Health")
        tags.add("Graduate Students")
        tags.add("Alumni")
    }
}