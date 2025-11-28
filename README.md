📱 College Event App

A simple and clean Android application built using Jetpack Compose and Firebase Realtime Database, designed for managing college events.
Users can add, edit, delete, and view event details with image support.

🚀 Features
✅ Add New Events

Event Name

Date

Description

Image URL

Data saved automatically in Firebase

✅ View Events (Home Screen)

Displays event list in card format

Shows event image, name, and date

Click an event to view full details

✅ Event Details Screen

Shows full event information

Displays event image in full width

Options:

✏ Edit Event

🗑 Delete Event

✅ Edit Event Screen

Allows editing all event fields

Updates data in Firebase

🔥 Firebase Integration

Realtime Database used to store:

id

name

date

description

imageUrl

🏗️ Tech Stack Used
Frontend / UI

🖼 Jetpack Compose

Material 3 Design Components

Coil (Image loading library)

Backend / Database

🔥 Firebase Realtime Database

Architecture

Simple state management using remember + LaunchedEffect

📂 Project Structure
CollegeEventApp/
 ├── MainActivity.kt
 ├── HomeScreen.kt
 ├── AddEventScreen.kt
 ├── EditEventScreen.kt
 ├── DetailEventScreen.kt
 ├── Event.kt (data model)
 └── ui/theme (Material 3 Theme files)

💡 How It Works
1. Home Screen

Loads all events using Firebase .get()
Displays events in a list using Jetpack Compose.

2. Add Event

User enters details → stored using .push().setValue().

3. Detail Screen

Displays event info + full-width image.

4. Edit Event

Loads event values → user updates → Firebase updates.

5. Delete

Removes event permanently using .removeValue().
