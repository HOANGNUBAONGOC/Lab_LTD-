package com.example.lab6

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab6.Course
import com.example.lab6.UpdateCourse
import com.example.lab6.ui.theme.Lab6Theme
import com.google.firebase.firestore.FirebaseFirestore
import coil.compose.rememberAsyncImagePainter


class CourseDetailsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val courseList = remember { mutableStateListOf<Course?>() }
            val db = FirebaseFirestore.getInstance()

            db.collection("Courses").get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    if (!queryDocumentSnapshots.isEmpty) {
                        val list = queryDocumentSnapshots.documents
                        for (d in list) {
                            val c: Course? = d.toObject(Course::class.java)
                            c?.courseID = d.id
                            Log.e("TAG", "Course id is : ${c?.courseID}")
                            courseList.add(c)
                        }
                    } else {
                        Toast.makeText(
                            this@CourseDetailsActivity,
                            "No data found in Database",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@CourseDetailsActivity,
                        "Fail to get the data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

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
                                        text = "Khóa học",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                })
                        }) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            Text(
                                text = "Cập nhật dữ liệu.",
                                modifier = Modifier.padding(8.dp)
                            )

                            Spacer(modifier = Modifier.height(15.dp))
                            firebaseUI(this@CourseDetailsActivity, courseList)
                        }
                    }
                }
            }
        }
    }
}

private fun deleteDataFromFirebase(courseID: String?, context: Context, courseList: MutableList<Course?>) {
    val db = FirebaseFirestore.getInstance()

    db.collection("Courses").document(courseID.toString()).delete().addOnSuccessListener {
        Toast.makeText(context, "Course Deleted successfully.", Toast.LENGTH_SHORT).show()
        courseList.removeIf { it?.courseID == courseID }
    }.addOnFailureListener {
        Toast.makeText(context, "Fail to delete course.", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun firebaseUI(context: Context, courseList: SnapshotStateList<Course?>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            itemsIndexed(courseList) { index, item ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            val intent = Intent(context, UpdateCourse::class.java)
                            intent.putExtra("courseName", item?.courseName)
                            intent.putExtra("courseDuration", item?.courseDuration)
                            intent.putExtra("courseDescription", item?.courseDescription)
                            intent.putExtra("courseID", item?.courseID)
                            context.startActivity(intent)
                        },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Spacer(modifier = Modifier.height(15.dp))
                        item?.courseName?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(4.dp),
                                color = Color.Green,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        item?.courseDuration?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(4.dp),
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontSize = 15.sp)
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        item?.courseDescription?.let { description ->
                            Text(
                                text = description,
                                modifier = Modifier.padding(4.dp),
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontSize = 15.sp)
                            )

                            if (description.startsWith("http")) {
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(104.dp)
                                        .clip(RoundedCornerShape(size = 8.dp)),
                                    painter = rememberAsyncImagePainter(description),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "Hình ảnh"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { deleteDataFromFirebase(item?.courseID, context, courseList) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = "Xóa khóa học", modifier = Modifier.padding(8.dp))
                        }
                     }
                }
            }
        }
    }
}

