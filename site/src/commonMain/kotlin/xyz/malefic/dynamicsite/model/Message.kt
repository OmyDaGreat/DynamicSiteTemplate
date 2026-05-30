package xyz.malefic.dynamicsite.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int,
    val text: String,
    val timestamp: Long,
)
