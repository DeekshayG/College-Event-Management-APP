package com.example.collegeeventapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToAddScreen: () -> Unit,
    goToDetail: (String) -> Unit,
    navController: NavController
) {
    val db = Firebase.database.reference
    val currentUser = Firebase.auth.currentUser

    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var userRole by remember { mutableStateOf("user") }
    val registrationCounts = remember { mutableStateMapOf<String, Int>() }
    var refreshing by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    val screenGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2b0b2d), Color(0xFF3a0f2f), Color(0xFF1a0814))
    )

    fun loadEvents() {
        refreshing = true
        db.child("events").get().addOnSuccessListener { snap ->
            val list = mutableListOf<Event>()
            snap.children.forEach { child ->
                child.getValue(Event::class.java)?.let { list.add(it) }
                db.child("registrations").child(child.key ?: "")
                    .get().addOnSuccessListener { regSnap ->
                        registrationCounts[child.key ?: ""] = regSnap.childrenCount.toInt()
                    }
            }
            events = list.sortedByDescending { it.date }
            refreshing = false
        }.addOnFailureListener { refreshing = false }
    }

    LaunchedEffect(currentUser) {
        loadEvents()
        currentUser?.uid?.let { uid ->
            db.child("users").child(uid).child("role").get()
                .addOnSuccessListener { snap ->
                    userRole = snap.getValue(String::class.java) ?: "user"
                }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(screenGradient)
                    .padding(top = 24.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.width(1.dp))

                    // Centered Large Title
                    Text(
                        text = "        College Events",
                        fontSize = 30.sp,              // Bigger
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    // Profile Button
                    Box {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.25f))
                                .clickable { menuExpanded = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFFFF6B6B),
                                                Color(0xFF9B59B6)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color(0xFFFFE6EC),  // soft pink
                                            Color(0xFFF4D9FF),  // soft lilac
                                        )
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(4.dp)
                        ) {

                            if (userRole == "admin") {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Add Event",
                                            color = Color(0xFF6A0D91),     // deep purple text
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = null,
                                            tint = Color(0xFFEA4C89)       // neon pink icon
                                        )
                                    },
                                    onClick = {
                                        menuExpanded = false
                                        goToAddScreen()
                                    }
                                )
                            }

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Logout",
                                        color = Color(0xFFE63946),        // soft red-pink
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.ExitToApp,
                                        contentDescription = null,
                                        tint = Color(0xFFE63946)
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    Firebase.auth.signOut()
                                    navController.navigate("signin") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }


                    }
                }
            }
            // --- END OF MODIFICATION ---
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(screenGradient)
                    .padding(padding)
            ) {
                if (refreshing) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFF6B6B))
                    }
                    return@Box
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(events) { event ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(300)),
                            exit = fadeOut()
                        ) {
                            NeonEventCard(
                                event = event,
                                registrationCount = registrationCounts[event.id] ?: 0,
                                onClick = { goToDetail(event.id) }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun NeonEventCard(event: Event, registrationCount: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 4.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0x44FF4D4D), Color(0x449B59B6))
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
        )

        Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0B12))
        ) {
            Column {
                if (event.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = event.imageUrl,
                        contentDescription = event.name,
                        modifier = Modifier.fillMaxWidth().height(170.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            event.name,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            event.date,
                            color = Color(0xFFFF6B6B),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        event.description.take(140) +
                                if (event.description.length > 140) "..." else "",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        maxLines = 3
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0x22FFFFFF)
                        ) {
                            Text(
                                " Registered: $registrationCount ",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Text(
                            "View details",
                            color = Color(0xFFFF6B6B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
