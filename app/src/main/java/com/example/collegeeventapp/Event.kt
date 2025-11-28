package com.example.collegeeventapp

data class Event(
    var id: String = "",
    var name: String = "",
    var date: String = "",
    var description: String = "",
    var imageUrl: String = ""    // <-- added
)
