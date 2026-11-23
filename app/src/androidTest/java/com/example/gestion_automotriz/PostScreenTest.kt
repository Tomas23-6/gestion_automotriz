package com.example.gestion_automotriz

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gestion_automotriz.data.model.Post
import com.example.gestion_automotriz.view.PostScreen
import com.example.gestion_automotriz.viewmodel.PostUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class PostScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun displaysPostsAndCreatesOneFromTheForm() {
        var savedPost: Pair<String, String>? = null
        composeRule.setContent {
            MaterialTheme {
                PostScreen(
                    state = PostUiState(
                        posts = listOf(
                            Post(userId = 1, id = 1, title = "Cambio de aceite", body = "Filtro y lubricante")
                        )
                    ),
                    onOpenDrawer = {},
                    onReload = {},
                    onCreate = { title, body -> savedPost = title to body },
                    onUpdate = { _, _, _ -> },
                    onDelete = {}
                )
            }
        }

        composeRule.onNodeWithText("Cambio de aceite").assertIsDisplayed()
        composeRule.onNodeWithText("Título").performTextInput("Nueva revisión")
        composeRule.onNodeWithText("Contenido").performTextInput("Inspección general")
        composeRule.onNodeWithText("Crear").performClick()

        assertEquals("Nueva revisión" to "Inspección general", savedPost)
    }

    @Test
    fun displaysApiErrorsToTheUser() {
        composeRule.setContent {
            MaterialTheme {
                PostScreen(
                    state = PostUiState(error = "Sin conexión"),
                    onOpenDrawer = {},
                    onReload = {},
                    onCreate = { _, _ -> },
                    onUpdate = { _, _, _ -> },
                    onDelete = {}
                )
            }
        }

        composeRule.onNodeWithText("Sin conexión").assertIsDisplayed()
    }
}
