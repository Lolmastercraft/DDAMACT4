package com.example.pruebas2.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pruebas2.data.Note
import com.example.pruebas2.ui.components.NoteCard
import com.example.pruebas2.viewModel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NoteViewModel,
    onNoteClick: (Note) -> Unit = {},
    onAddNoteClick: () -> Unit = {}
) {
    val notes by viewModel.notes.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var deletedNote by remember { mutableStateOf<Note?>(null) }

    deletedNote?.let { note ->
        LaunchedEffect(note) {
            val result = snackBarHostState.showSnackbar(
                message = "Nota eliminada",
                actionLabel = "Deshacer"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.addNote(note)
            }
            deletedNote = null
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Notas") }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar nota")
            }
        }
    ) { padding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se han agregado notas aún")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(notes, key = { it.id }) { note ->
                    var visible by remember { mutableStateOf(true) }

                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally() + fadeIn(),
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    visible = false
                                    viewModel.deleteNote(note)
                                    deletedNote = note
                                    true
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            content = {
                                NoteCard(
                                    note = note,
                                    onclick = { onNoteClick(note) }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}