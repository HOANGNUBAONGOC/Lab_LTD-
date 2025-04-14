package com.example.notessqlite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notessqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    private val notesList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Tắt animation khi khởi động
        overridePendingTransition(0, 0)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing binding", e)
            return
        }

        try {
            db = NotesDatabaseHelper(this)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing database", e)
            return
        }

        try {
            notesList.addAll(db.getAllNotes())
            notesAdapter = NotesAdapter(notesList)
            binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.notesRecyclerView.adapter = notesAdapter
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing adapter", e)
            return
        }

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        updateEmptyStateVisibility()
    }

    override fun onResume() {
        super.onResume()
        try {
            Log.d("MainActivity", "onResume: Updating notes list")
            val updatedNotes = db.getAllNotes()
            Log.d("MainActivity", "onResume: Fetched ${updatedNotes.size} notes")
            notesList.clear()
            notesList.addAll(updatedNotes)
            notesAdapter.refreshData(updatedNotes)
            updateEmptyStateVisibility()
            Log.d("MainActivity", "onResume: Notes list updated successfully")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onResume", e)
            Toast.makeText(this, "Error updating notes: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateEmptyStateVisibility() {
        try {
            if (notesList.isEmpty()) {
                binding.notesRecyclerView.visibility = View.GONE
                binding.emptyTextView?.visibility = View.VISIBLE
            } else {
                binding.notesRecyclerView.visibility = View.VISIBLE
                binding.emptyTextView?.visibility = View.GONE
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in updateEmptyStateVisibility", e)
        }
    }
}