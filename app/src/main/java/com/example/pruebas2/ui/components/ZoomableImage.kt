package com.example.pruebas2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter

@Composable
fun ZoomableImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var showFullScreen by remember { mutableStateOf(false) }

    // Imagen en miniatura clickeable
    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = contentDescription,
        modifier = modifier
            .size(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    if (zoom > 1.1f) { // Detectar zoom para abrir pantalla completa
                        showFullScreen = true
                    }
                }
            },
        contentScale = ContentScale.Crop
    )

    // Dialog de pantalla completa con zoom
    if (showFullScreen) {
        Dialog(
            onDismissRequest = { showFullScreen = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y
                        )
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 5f)
                                offset = if (scale == 1f) {
                                    Offset.Zero
                                } else {
                                    offset + pan
                                }
                            }
                        },
                    contentScale = ContentScale.Fit
                )

                // Botón cerrar
                Button(
                    onClick = {
                        showFullScreen = false
                        scale = 1f
                        offset = Offset.Zero
                    },
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
fun ImageGallery(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    if (images.isNotEmpty()) {
        LazyRow(
            modifier = modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(images) { imageUrl ->
                ZoomableImage(
                    imageUrl = imageUrl,
                    contentDescription = "Imagen adjunta"
                )
            }
        }
    }
}