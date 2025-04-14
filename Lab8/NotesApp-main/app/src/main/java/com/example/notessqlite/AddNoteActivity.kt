package com.example.notessqlite

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper
    private var fileUri: Uri? = null

    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            fileUri = it
            val mimeType = contentResolver.getType(it)
            if (mimeType?.startsWith("image") == true) {
                binding.imagePreview.setImageURI(it)
                binding.imagePreview.visibility = android.view.View.VISIBLE
            } else {
                binding.imagePreview.visibility = android.view.View.GONE
            }
            Toast.makeText(this, "File selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityAddNoteBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Exception) {
            Log.e("AddNoteActivity", "Error initializing binding", e)
            Toast.makeText(this, "Error initializing UI: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        try {
            db = NotesDatabaseHelper(this)
        } catch (e: Exception) {
            Log.e("AddNoteActivity", "Error initializing database", e)
            Toast.makeText(this, "Error initializing database: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.uploadImageButton.setOnClickListener {
            pickFileLauncher.launch("*/*")
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.contentEditText.text.toString()
            val filePath = fileUri?.toString()

            if (title.isNotEmpty()) {
                try {
                    val note = Note(0, title, description, filePath)
                    Log.d("AddNoteActivity", "Saving note: $note")
                    db.insertNote(note)
                    Log.d("AddNoteActivity", "Note saved successfully")
                    Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Log.e("AddNoteActivity", "Error saving note", e)
                    Toast.makeText(this, "Error saving note: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun finish() {
        try {
            Log.d("AddNoteActivity", "Finishing AddNoteActivity")
            super.finish()
            // Tắt animation khi thoát
            overridePendingTransition(0, 0)
        } catch (e: Exception) {
            Log.e("AddNoteActivity", "Error in finish", e)
        }
    }
}