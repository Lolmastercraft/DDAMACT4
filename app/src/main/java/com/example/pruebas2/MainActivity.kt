package com.example.pruebas2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.pruebas2.ui.navigation.AppNavHost
import com.example.pruebas2.ui.theme.Pruebas2Theme
import com.example.pruebas2.viewModel.NoteViewModel
import com.example.pruebas2.data.NoteDataBase
import com.example.pruebas2.repository.NoteRepository
import com.example.pruebas2.viewModel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = Room.databaseBuilder(
            applicationContext,
            NoteDataBase::class.java,
            "note_db"
        )
            .addMigrations(
                NoteDataBase.MIGRATION_1_2,
                NoteDataBase.MIGRATION_2_3
            )
            .build()

        val repository = NoteRepository(database.noteDao())
        val viewModelFactory = NoteViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]

        setContent {
            Pruebas2Theme {
                AppNavHost(viewModel = viewModel)
            }
        }
    }
}