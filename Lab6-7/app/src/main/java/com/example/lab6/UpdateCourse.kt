package com.example.lab6

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab6.ui.theme.Lab6Theme
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCourse : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Update Course",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            )
                        }
                    ) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            firebaseUI(
                                LocalContext.current,
                                intent.getStringExtra("courseName"),
                                intent.getStringExtra("courseDuration"),
                                intent.getStringExtra("courseDescription"),
                                intent.getStringExtra("courseID")
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun firebaseUI(
        context: Context,
        name: String?,
        duration: String?,
        description: String?,
        courseID: String?
    ) {
        val courseName = remember { mutableStateOf(name ?: "") }
        val courseDuration = remember { mutableStateOf(duration ?: "") }
        val courseDescription = remember { mutableStateOf(description ?: "") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = courseName.value,
                onValueChange = { courseName.value = it },
                placeholder = { Text(text = "Enter course name") },
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = courseDuration.value,
                onValueChange = { courseDuration.value = it },
                placeholder = { Text(text = "Enter course duration") },
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = courseDescription.value,
                onValueChange = { courseDescription.value = it },
                placeholder = { Text(text = "Enter course description") },
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (TextUtils.isEmpty(courseName.value)) {
                        Toast.makeText(context, "Please enter course name", Toast.LENGTH_SHORT).show()
                    } else if (TextUtils.isEmpty(courseDuration.value)) {
                        Toast.makeText(context, "Please enter course duration", Toast.LENGTH_SHORT).show()
                    } else if (TextUtils.isEmpty(courseDescription.value)) {
                        Toast.makeText(context, "Please enter course description", Toast.LENGTH_SHORT).show()
                    } else {
                        updateDataToFirebase(
                            courseID ?: "",
                            courseName.value,
                            courseDuration.value,
                            courseDescription.value,
                            context
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Update Data", modifier = Modifier.padding(8.dp))
            }
        }
    }

    private fun updateDataToFirebase(
        courseID: String,
        name: String,
        duration: String,
        description: String,
        context: Context
    ) {
        val updatedCourse = Course(
            courseID, name, duration, description,
            description = TODO()
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("Courses").document(courseID).set(updatedCourse)
            .addOnSuccessListener {
                Toast.makeText(context, "Course updated successfully", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, CourseDetailsActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update course: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
