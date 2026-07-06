package xyz.malefic.dynamicsite.client.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import xyz.malefic.dynamicsite.common.json
import xyz.malefic.dynamicsite.common.model.Message

@Page
@Composable
fun MessagesPage() {
    val messages = remember { mutableStateOf<List<Message>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = window.fetch("/api/messages").await()

            if (response.ok) {
                val string = response.text().await()
                val parsed = json.decodeFromString<List<Message>>(string)
                messages.value = parsed
                isLoading.value = false
            } else {
                error.value = "Failed to fetch: ${response.status}"
                isLoading.value = false
            }
        } catch (e: Exception) {
            error.value = "Error: ${e.message}"
            isLoading.value = false
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(Modifier.padding(16.px)) {
            when {
                isLoading.value -> {
                    Text("Loading messages...")
                }

                error.value != null -> {
                    Text("Error: ${error.value}")
                }

                messages.value.isEmpty() -> {
                    Text("No messages")
                }

                else -> {
                    Text("Messages from Server (${messages.value.size})")
                    messages.value.forEach { msg ->
                        Box(Modifier.padding(8.px)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.px),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text("ID: ${msg.id}")
                                Text("Text: ${msg.text}")
                                Text("Time: ${msg.timestamp}")
                            }
                        }
                    }
                }
            }
        }
    }
}
