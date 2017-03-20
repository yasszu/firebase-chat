package com.example.firebasechat.repository

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import java.util.*

/**
 * Created by Yasuhiro Suzuki on 2017/03/20.
 *
 * Firebase Database
 */
class FirebaseData(val reference: DatabaseReference) : ChildEventListener {

    val snapshots = ArrayList<DataSnapshot>()

    init {
        reference.addChildEventListener(this)
    }

    override fun onChildAdded(snapshot: DataSnapshot?, previousChildKey: String?) {
        if (snapshot == null) return
        val index = 0
        snapshots.add(index, snapshot)
    }

    override fun onChildChanged(snapshot: DataSnapshot?, previousChildKey: String?) {
        if (snapshot == null) return
        val index = getIndexForKey(snapshot.key)
        snapshots[index] = snapshot
    }

    override fun onChildRemoved(snapshot: DataSnapshot?) {
        if (snapshot == null) return
        val index = getIndexForKey(snapshot.key)
        snapshots.removeAt(index)
    }

    override fun onChildMoved(snapshot: DataSnapshot?, previousChildKey: String?) {
        if (snapshot == null) return
        val oldIndex = getIndexForKey(snapshot.key)
        snapshots.removeAt(oldIndex)
        val newIndex = if (previousChildKey == null) 0 else getIndexForKey(previousChildKey) + 1
        snapshots.add(newIndex, snapshot)
    }

    override fun onCancelled(snapshot: DatabaseError?) {
    }

    private fun getIndexForKey(key: String): Int {
        return snapshots.indices.filter { snapshots[it].key == key }.last()
    }

}