# 🎓 College Event Management App

A simple Android app built using **Jetpack Compose** and **Firebase Realtime Database** for managing college events.  
Users can **add, view, edit, and delete** events with image support.

---

## ⭐ Features

### 📌 Add Event
- Add event name  
- Select date  
- Add description  
- Add image URL  
- Save directly to Firebase

### 📌 View Events
- List of all events  
- Each event card shows:
  - Image  
  - Name  
  - Date  
- Click any event to view full details

### 📌 Event Detail Page
- Full image preview  
- Event name, date, and description  
- Buttons to:
  - **Edit event**
  - **Delete event**

### 📌 Edit Event
- Update all event fields  
- Save changes to Firebase

---

## 🛠️ Technologies Used

| Component | Technology |
|----------|------------|
| UI | Jetpack Compose + Material 3 |
| Database | Firebase Realtime Database |
| Language | Kotlin |
| Image Loading | Coil |
| Navigation | Jetpack Navigation Compose |

---

## 📁 Project Structure

/com.example.collegeeventapp
├── MainActivity.kt
├── HomeScreen.kt
├── AddEventScreen.kt
├── DetailEventScreen.kt
├── EditEventScreen.kt
├── Event.kt
└── ui/theme/*


---

## ⚙️ How It Works

### 1⃣ Add Event  
Form input → Generate ID → Upload to Firebase.

### 2⃣ Home List  
Fetch all events → Display in a LazyColumn list.

### 3⃣ Detail Page  
Shows event information + mini toolbar with Edit & Delete options.

### 4⃣ Edit Event  
Load event → User edits → Update Firebase.

### 5⃣ Delete Event  
Removes event instantly from Firebase.
