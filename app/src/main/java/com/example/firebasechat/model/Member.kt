package com.example.firebasechat.model

/**
 * Firebase Realtime database model
 */
data class Member(
        var id: Int = -1,
        var name: String = "",
        var thumb: String="")