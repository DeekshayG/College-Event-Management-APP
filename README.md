🎓 College Event Management App

A simple Android app built using Jetpack Compose and Firebase Realtime Database for managing college events.
This app allows users to add, view, edit, and delete events with an image preview.

⭐ Features

📌 Add Event
       Enter event name
       Enter date
       Add description
       Add an image URL
       Save directly to Firebase

📌 View Events (Home Screen)
       All events shown in a list
       Each card displays:
       Event name
       Date
       Event image
       Click any event to open detailed view

📌 Event Details Page
       Shows full event information
       Displays image in full width
       Options:
       Edit the event
       Delete the event

📌 Edit Event
       Edit all fields of an existing event
       Saves back to Firebase

🛠️ Technologies Used

Area	                          Technology
UI -----------------------Jetpack Compose + Material 3
Image --------------------Loading	Coil
Database------------------Firebase Realtime Database
Language	-----------------Kotlin
Architecture--------------Simple - State management using remember & LaunchedEffect


📁 Project Files

/com.example.collegeeventapp
 ├── MainActivity.kt
 ├── HomeScreen.kt
 ├── AddEventScreen.kt
 ├── DetailEventScreen.kt
 ├── EditEventScreen.kt
 ├── Event.kt
 └── ui/theme/*
 

⚙️ How the App Works

📝 Add Event
User enters details → App generates an ID → Saves to Firebase.

📖 View Events
App loads all events from Firebase and displays them in a list.

🔍 Detail Page
Shows image, title, date, description + options to edit or delete.

✏ Edit Event
Loads existing data → user updates → saves changes to Firebase.

❌ Delete Event
Removes event from Firebase instantly.
