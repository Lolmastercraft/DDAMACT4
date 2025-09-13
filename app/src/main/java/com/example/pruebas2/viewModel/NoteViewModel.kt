package com.example.pruebas2.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebas2.data.Note
import com.example.pruebas2.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())

    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllNotes().collectLatest { notesFromDb ->
                _notes.value = notesFromDb
            }
        }
    }

    fun addNote(note:Note) {
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun getNoteById(id: Long): Note? {
        return notes.value.find { it.id == id}
    }
}