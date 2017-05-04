package com.example.firebasechat.repository

import com.google.firebase.auth.FirebaseAuth

/**
 * Created by Yasuhiro Suzuki on 2017/05/04.
 */
object SessionRepository {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val uid: String?
        get() = auth.currentUser?.uid

}
