package com.example.firebasechat.model

/**
 * Firebase Realtime database model
 */
data class Message(var userId: Int = -1,
                   var body: String = "",
                   var timestamp: Long = 0,
                   var saved: Boolean = false)