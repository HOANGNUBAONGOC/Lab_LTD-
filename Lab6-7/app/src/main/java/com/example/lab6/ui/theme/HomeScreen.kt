package com.example.lab6.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase

@Composable
fun HomeScreen() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("message")

    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text(text = "Enter your data") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                myRef.setValue(text)
                    .addOnSuccessListener { println("Data saved successfully!") }
                    .addOnFailureListener { e -> println("Error saving data: ${e.message}") }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Submit")
        }
    }
}
