package com.example.collegeeventapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEventScreen(id: String, goBack: () -> Unit, goEdit: (String) -> Unit) {

    val screenGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2b0b2d), Color(0xFF3a0f2f), Color(0xFF1a0814))
    )

    var event by remember { mutableStateOf<Event?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var userRole by remember { mutableStateOf("user") }
    var registered by remember { mutableStateOf(false) }

    val currentUser = Firebase.auth.currentUser
    val db = Firebase.database.reference

    LaunchedEffect(id) {
        db.child("events").child(id).get().addOnSuccessListener {
            event = it.getValue(Event::class.java)
        }

        currentUser?.uid?.let { uid ->
            db.child("users").child(uid).child("role").get()
                .addOnSuccessListener { snap ->
                    userRole = snap.getValue(String::class.java) ?: "user"
                }
            db.child("registrations").child(id).child(uid).get()
                .addOnSuccessListener { snap ->
                    registered = snap.exists()
                }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Event Details",
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    if (userRole == "admin") {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = Color(0xFFFF6B6B)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color(0x44FFB6C1),
                                            Color(0x33FFC0CB)
                                        )
                                    ),
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .width(160.dp)
                        ) {

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Edit Event Details",
                                        color = Color(0xFF6A0D91),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    goEdit(id)
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Delete Event",
                                        color = Color(0xFFFF6B6B),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { pad ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(screenGradient)
                .padding(pad)
                .padding(16.dp)
        ) {

            item {
                if (event == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }

            event?.let { e ->

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {

                            if (e.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = e.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                )
                                Spacer(Modifier.height(12.dp))
                            }

                            Text(
                                e.name,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "Date: ${e.date}",
                                color = Color(0xFFFF6B6B),
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(Modifier.height(12.dp))

                            Text(
                                e.description,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))
                }

                if (userRole == "user") {
                    item {
                        Button(
                            onClick = {
                                currentUser?.uid?.let { uid ->
                                    db.child("registrations").child(id).child(uid)
                                        .setValue(true)
                                        .addOnSuccessListener { registered = true }
                                }
                            },
                            enabled = !registered,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B))
                        ) {
                            Text(
                                if (registered) "Registered" else "Register",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirm Delete", color = Color.White) },
                text = { Text("Are you sure you want to delete this event?", color = Color.White) },
                containerColor = Color(0xFF2b0b2d),
                confirmButton = {
                    TextButton(onClick = {
                        db.child("events").child(id).removeValue()
                        showDeleteDialog = false
                        goBack()
                    }) {
                        Text("Yes", color = Color(0xFFFF6B6B))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel", color = Color.White)
                    }
                }
            )
        }
    }
}
