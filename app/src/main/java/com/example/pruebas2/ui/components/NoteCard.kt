package com.example.pruebas2.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.pruebas2.data.Note
import java.time.format.DateTimeFormatter

@Composable
fun NoteCard(
    note: Note,
    modifier: Modifier = Modifier,
    onclick: () -> Unit = {}
) {
    Card (
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onclick
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = modifier.height(4.dp))

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = modifier.height(4.dp))

            Text(
                text = note.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH: mm")),
                style = MaterialTheme.typography.labelSmall,
            )

            Spacer(modifier = modifier.height(4.dp))
        }
    }
}