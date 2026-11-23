package com.example.gestion_automotriz.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestion_automotriz.data.model.Post
import com.example.gestion_automotriz.viewmodel.PostUiState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PostScreen(
    state: PostUiState,
    onOpenDrawer: () -> Unit,
    onReload: () -> Unit,
    onCreate: (String, String) -> Unit,
    onUpdate: (Int, String, String) -> Unit,
    onDelete: (Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var editingPost by remember { mutableStateOf<Post?>(null) }
    var deletingPost by remember { mutableStateOf<Post?>(null) }

    LaunchedEffect(state.message) {
        if (state.message == "Publicación guardada") {
            title = ""
            body = ""
            editingPost = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicaciones de API REST") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Text("☰", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            )
            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("Contenido") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val post = editingPost
                        if (post?.id == null) onCreate(title, body)
                        else onUpdate(post.id, title, body)
                    },
                    enabled = !state.isLoading && title.isNotBlank() && body.isNotBlank()
                ) {
                    Text(if (editingPost == null) "Crear" else "Guardar cambios")
                }
                if (editingPost != null) {
                    OutlinedButton(
                        onClick = {
                            editingPost = null
                            title = ""
                            body = ""
                        },
                        enabled = !state.isLoading
                    ) {
                        Text("Cancelar")
                    }
                }
                OutlinedButton(onClick = onReload, enabled = !state.isLoading) {
                    Text("Actualizar")
                }
            }

            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            if (state.isLoading) Text("Cargando...")

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.posts, key = { it.id ?: it.hashCode() }) { post ->
                    PostCard(
                        post = post,
                        enabled = !state.isLoading,
                        onEdit = {
                            editingPost = post
                            title = post.title
                            body = post.body
                        },
                        onDelete = { deletingPost = post }
                    )
                }
            }
        }
    }

    deletingPost?.let { post ->
        AlertDialog(
            onDismissRequest = { deletingPost = null },
            title = { Text("Eliminar publicación") },
            text = { Text("¿Deseas eliminar “${post.title}”?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        post.id?.let(onDelete)
                        deletingPost = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingPost = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun PostCard(
    post: Post,
    enabled: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(post.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(post.body)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onEdit, enabled = enabled && post.id != null) {
                    Text("Editar")
                }
                TextButton(onClick = onDelete, enabled = enabled && post.id != null) {
                    Text("Eliminar")
                }
            }
        }
    }
}
