package com.example.notessqlite

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.databinding.ActivityUpdateNoteBinding

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1
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
            Toast.makeText(this, "File updated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Exception) {
            Log.e("UpdateNoteActivity", "Error initializing binding", e)
            finish()
            return
        }

        try {
            db = NotesDatabaseHelper(this)
        } catch (e: Exception) {
            Log.e("UpdateNoteActivity", "Error initializing database", e)
            finish()
            return
        }

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        if (note == null) {
            finish()
            return
        }

        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.description)
        note.file?.let { filePath ->
            try {
                fileUri = Uri.parse(filePath)
                val mimeType = contentResolver.getType(fileUri!!)
                if (mimeType?.startsWith("image") == true) {
                    binding.imagePreview.setImageURI(fileUri)
                    binding.imagePreview.visibility = android.view.View.VISIBLE
                }
            } catch (e: Exception) {
                binding.imagePreview.visibility = android.view.View.GONE
                Toast.makeText(this, "Error loading file", Toast.LENGTH_SHORT).show()
            }
        }

        binding.uploadImageButton.setOnClickListener {
            pickFileLauncher.launch("*/*")
        }

        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newDescription = binding.updateContentEditText.text.toString()
            val filePath = fileUri?.toString()

            if (newTitle.isNotEmpty()) {
                try {
                    val updatedNote = Note(noteId, newTitle, newDescription, filePath)
                    db.updateNote(updatedNote)
                    Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Log.e("UpdateNoteActivity", "Error saving note", e)
                    Toast.makeText(this, "Error saving note: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun finish() {
        try {
            Log.d("UpdateNoteActivity", "Finishing UpdateNoteActivity")
            super.finish()
            overridePendingTransition(0, 0)
        } catch (e: Exception) {
            Log.e("UpdateNoteActivity", "Error in finish", e)
        }
    }
}