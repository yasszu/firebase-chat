package com.example.firebasechat.repository

import com.example.firebasechat.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Yasuhiro Suzuki on 2017/07/23.
 */
object SessionRepository {

    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    val uid: String?
        get() = currentUser?.uid

    val isLogin: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null

    fun isSelf(uid: String) = this.uid == uid

    fun isSelf(message: Message) = isSelf(message.uid)

}