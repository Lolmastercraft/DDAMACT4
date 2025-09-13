package com.example.pruebas2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebas2.ui.screens.AddEditNoteScreen
import com.example.pruebas2.ui.screens.NoteListScreen
import com.example.pruebas2.viewModel.NoteViewModel

@Composable
fun AppNavHost(
    viewModel: NoteViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "note_list"
    ) {
        composable("note_list") {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = { note ->
                    navController.navigate("edit_note/${note.id}")
                },
                onAddNoteClick = {
                    navController.navigate("add_note")
                }
            )
        }

        composable("add_note") {
            AddEditNoteScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("edit_note/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toLongOrNull()
            val notes = viewModel.notes.collectAsState().value
            val note = notes.find { it.id == noteId }

            AddEditNoteScreen(
                viewModel = viewModel,
                existingNote = note,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}