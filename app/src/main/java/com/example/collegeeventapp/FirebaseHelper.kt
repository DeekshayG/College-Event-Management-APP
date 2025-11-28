package com.example.collegeeventapp

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FirebaseHelper {

    private val database = FirebaseDatabase.getInstance()
    private val eventsRef = database.getReference("events")

    fun getEvents(): LiveData<List<Event>> {
        val liveData = MutableLiveData<List<Event>>()

        eventsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val events = mutableListOf<Event>()
                for (child in snapshot.children) {
                    val event = child.getValue(Event::class.java)
                    if (event != null) {
                        events.add(event)
                    }
                }
                liveData.value = events
            }

            override fun onCancelled(error: DatabaseError) {
                liveData.value = emptyList()
            }
        })

        return liveData
    }
}
