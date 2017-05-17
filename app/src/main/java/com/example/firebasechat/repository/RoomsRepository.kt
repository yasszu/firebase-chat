package com.example.firebasechat.repository

import com.example.firebasechat.model.Room
import com.example.firebasechat.model.Snapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Yasuhiro Suzuki on 2017/05/03.
 */
object RoomsRepository {

    val ROOMS_CHILD = "rooms"

    var onSnapshotsUpdate: () -> Unit = {}

    val reference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference(ROOMS_CHILD)
        .child(SessionRepository.uid)

    val snapshot: Snapshot

    init {
       snapshot = Snapshot(reference).apply {
           onDataAdded = { onSnapshotsUpdate() }
           onDataChanged = { onSnapshotsUpdate() }
           onDataRemoved = { onSnapshotsUpdate() }
           onDataMoved = { onSnapshotsUpdate() }
       }
    }

    fun post(room: Room) {
        if (room.id.isNullOrEmpty()) return
        reference.push().setValue(room)
    }

    fun findAll(): List<Room> = snapshot.getValues()

    fun removeListener() {
        onSnapshotsUpdate = {}
    }

}
