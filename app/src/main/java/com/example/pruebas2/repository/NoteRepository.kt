package com.example.pruebas2.repository

import com.example.pruebas2.data.Note
import com.example.pruebas2.data.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepository (private val noteDao: NoteDao) {
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun delete(note: Note) {
        noteDao.deleteNote(note)
    }
}