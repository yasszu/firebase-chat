package com.example.firebasechat.repository

import com.example.firebasechat.model.Message
import com.example.firebasechat.model.Snapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Yasuhiro Suzuki on 2017/03/20.
 */
class MessagesRepository(roomId: String) {

    val MESSAGES_CHILD = "messages"

    val reference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference(MESSAGES_CHILD)
        .child(roomId)

    val snapshot: Snapshot by lazy { Snapshot(reference) }

    val timeStamp: Long
        get() = System.currentTimeMillis()

    fun post(message: Message) {
        reference.push().setValue(message)
    }

    fun findAll(): List<Message> = snapshot.getValues()

}
