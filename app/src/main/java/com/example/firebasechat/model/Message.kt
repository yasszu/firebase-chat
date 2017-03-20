package com.example.firebasechat.model

/**
 * Firebase Realtime database model
 */
data class Message(var uid: String = "",
                   var body: String = "",
                   var timestamp: Long = 0,
                   var saved: Boolean = false)