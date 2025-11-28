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
import coil.compose.AsyncImage
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(goBack: () -> Unit) {

    // Neon background gradient (same as HomeScreen)
    val screenGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2b0b2d), Color(0xFF3a0f2f), Color(0xFF1a0814))
    )

    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imgUrl by remember { mutableStateOf("") }
    val db = Firebase.database.reference
    var saving by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Add Event",
                        color = Color(0xFFFF6B6B),     // neon heading color
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
            modifier = Modifier
                .fillMaxSize()
                .background(screenGradient)   // Apply neon background
                .padding(pad)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Event Name", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
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
                label = { Text("Date (e.g. 2025-12-31)", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B6B),
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color(0xFFFF6B6B)
                )
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = imgUrl,
                onValueChange = { imgUrl = it },
                label = { Text("Image URL (optional)", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
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
                shape = MaterialTheme.shapes.medium,
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B6B),
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color(0xFFFF6B6B)
                )
            )

            Spacer(Modifier.height(14.dp))

            if (imgUrl.isNotBlank()) {
                AsyncImage(
                    model = imgUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if (name.isBlank() || date.isBlank()) return@Button
                    saving = true
                    val id = db.child("events").push().key ?: System.currentTimeMillis().toString()

                    db.child("events").child(id)
                        .setValue(Event(id, name, date, description, imgUrl))
                        .addOnSuccessListener {
                            saving = false
                            goBack()
                        }
                        .addOnFailureListener { saving = false }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B6B)
                )
            ) {
                if (saving)
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White
                    )
                else
                    Text(
                        "Save Event",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
            }
        }
    }
}
