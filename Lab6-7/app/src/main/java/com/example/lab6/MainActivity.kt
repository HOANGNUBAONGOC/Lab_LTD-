package com.example.lab6

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab6.CourseDetailsActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseUI()
        }
    }
}

@Composable
fun FirebaseUI() {
    val context = LocalContext.current
    val courseName = remember { mutableStateOf("") }
    val courseDuration = remember { mutableStateOf("") }
    val courseDescription = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = courseName.value,
            onValueChange = { courseName.value = it },
            placeholder = { Text("Enter your course name") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = courseDuration.value,
            onValueChange = { courseDuration.value = it },
            placeholder = { Text("Enter your course duration") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = courseDescription.value,
            onValueChange = { courseDescription.value = it },
            placeholder = { Text("Enter your course description") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (courseName.value.isEmpty()) {
                    Toast.makeText(context, "Please enter course name", Toast.LENGTH_SHORT).show()
                } else if (courseDuration.value.isEmpty()) {
                    Toast.makeText(context, "Please enter course duration", Toast.LENGTH_SHORT).show()
                } else if (courseDescription.value.isEmpty()) {
                    Toast.makeText(context, "Please enter course description", Toast.LENGTH_SHORT).show()
                } else {
                    addDataToFirebase(courseName.value, courseDuration.value, courseDescription.value, context)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Data", modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                context.startActivity(Intent(context, CourseDetailsActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Courses", modifier = Modifier.padding(8.dp))
        }
    }
}


fun addDataToFirebase(name: String, duration: String, description: String, context: Context) {
    val db = FirebaseFirestore.getInstance()
    val course = hashMapOf(
        "name" to name,
        "duration" to duration,
        "description" to description
    )

    db.collection("courses")
        .add(course)
        .addOnSuccessListener {
            Toast.makeText(context, "Course added successfully!", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to add course!", Toast.LENGTH_SHORT).show()
        }
}
