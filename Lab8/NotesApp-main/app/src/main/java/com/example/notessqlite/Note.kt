package com.example.notessqlite

data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val file: String? = null
)