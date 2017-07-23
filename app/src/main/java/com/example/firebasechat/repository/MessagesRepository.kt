package com.example.firebasechat.repository

import com.example.firebasechat.model.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Yasuhiro Suzuki on 2017/03/20.
 */
class MessagesRepository(val roomId: Int) {

    val MESSAGES_CHILD = "messages"

    val reference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance()
                .getReference(MESSAGES_CHILD)
                .child(roomId.toString())
    }

    val firebaseData: FirebaseData

    val timeStamp: Long
        get() = System.currentTimeMillis()

    init {
        firebaseData = FirebaseData(reference)
    }

    fun post(message: Message) {
        reference.push().setValue(message)
    }

    fun setSaved(ref: DatabaseReference, message: Message) {
        ref.setValue(Message(message.uid, message.body, message.timestamp, !message.saved))
    }

    fun delete(ref: DatabaseReference) {
        ref.removeValue()
    }

    fun findAll(): List<Message> = firebaseData.snapshots.map { it.getValue(Message::class.java) }

}