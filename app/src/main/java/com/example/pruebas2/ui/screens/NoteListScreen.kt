package com.example.pruebas2.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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

    // Configuración responsive
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = screenWidth > 600.dp

    // Feedback de eliminación con Snackbar
    deletedNote?.let { note ->
        LaunchedEffect(note) {
            val result = snackBarHostState.showSnackbar(
                message = "Nota eliminada",
                actionLabel = "Deshacer",
                duration = SnackbarDuration.Short
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
                title = {
                    Text("Mis Notas (${notes.size})")
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddNoteClick,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text(if (isTablet) "Agregar nueva nota" else "Agregar") }
            )
        }
    ) { padding ->
        if (notes.isEmpty()) {
            // Estado vacío
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No se han agregado notas aún",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onAddNoteClick) {
                        Text("Crear primera nota")
                    }
                }
            }
        } else {
            // Lista responsive de notas
            if (isTablet) {
                // Grid para tablets
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(notes, key = { it.id }) { note ->
                        NoteItemWithAnimations(
                            note = note,
                            onNoteClick = onNoteClick,
                            onDelete = { deletedNoteParam ->
                                viewModel.deleteNote(deletedNoteParam)
                                deletedNote = deletedNoteParam
                            }
                        )
                    }
                }
            } else {
                // Lista vertical para móviles
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(notes, key = { it.id }) { note ->
                        NoteItemWithAnimations(
                            note = note,
                            onNoteClick = onNoteClick,
                            onDelete = { deletedNoteParam ->
                                viewModel.deleteNote(deletedNoteParam)
                                deletedNote = deletedNoteParam
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteItemWithAnimations(
    note: Note,
    onNoteClick: (Note) -> Unit,
    onDelete: (Note) -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    ) {
        val dismissState = rememberSwipeToDismissBoxState(
            confirmValueChange = { dismissValue ->
                when (dismissValue) {
                    SwipeToDismissBoxValue.EndToStart -> {
                        visible = false
                        onDelete(note)
                        true
                    }
                    else -> false
                }
            }
        )

        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
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