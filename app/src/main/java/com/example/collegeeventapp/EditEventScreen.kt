package com.example.collegeeventapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(id: String, goBack: () -> Unit) {

    // Same neon gradient as other screens
    val screenGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2b0b2d),
            Color(0xFF3a0f2f),
            Color(0xFF1a0814)
        )
    )

    val db = Firebase.database.reference
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var saving by remember { mutableStateOf(false) }

    // Load event data
    LaunchedEffect(id) {
        db.child("events").child(id).get().addOnSuccessListener { snap ->
            val e = snap.getValue(Event::class.java)
            e?.let {
                name = it.name
                date = it.date
                description = it.description
                imageUrl = it.imageUrl
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Edit Event",
                        color = Color(0xFFFF6B6B),  // neon pink heading
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { pad ->

        Column(
            Modifier
                .fillMaxSize()
                .background(screenGradient) // apply neon gradient
                .padding(pad)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Event Name", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B6B),
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color(0xFFFF6B6B)
                )
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B6B),
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color(0xFFFF6B6B)
                )
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B6B),
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color(0xFFFF6B6B)
                )
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Image URL", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B6B),
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color(0xFFFF6B6B)
                )
            )

            Spacer(Modifier.height(12.dp))

            if (imageUrl.isNotBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
                Spacer(Modifier.height(10.dp))
            }

            Button(
                onClick = {
                    saving = true
                    val updated = Event(id, name, date, description, imageUrl)

                    db.child("events").child(id).setValue(updated)
                        .addOnSuccessListener {
                            saving = false
                            goBack()
                        }
                        .addOnFailureListener { saving = false }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B6B) // neon pink button
                )
            ) {
                if (saving)
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White
                    )
                else
                    Text("Update", fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}
