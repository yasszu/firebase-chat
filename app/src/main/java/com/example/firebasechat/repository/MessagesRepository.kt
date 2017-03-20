package com.example.firebasechat.repository

import com.example.firebasechat.model.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Yasuhiro Suzuki on 2017/03/20.
 */
class MessagesRepository(val roomId: Int) {

    val MESSAGES_CHILD = "messages"

    val reference: DatabaseReference
        get() = FirebaseDatabase.getInstance()
                .getReference(MESSAGES_CHILD)
                .child(roomId.toString())

    val firebaseData: FirebaseData

    init {
        firebaseData = FirebaseData(reference)
    }

    fun write(message: Message) {
        reference.push().setValue(message)
    }

    /** Get current snapshots **/
    fun findAll(): List<Message> = firebaseData.snapshots.map { it.getValue(Message::class.java) }

}