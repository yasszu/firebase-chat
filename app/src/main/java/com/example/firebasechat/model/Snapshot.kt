package com.example.firebasechat.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import java.util.*

/**
 * Created by Yasuhiro Suzuki on 2017/03/20.
 *
 * Firebase Realtime Database
 * See https://firebase.google.com/docs/database/android/retrieve-data
 */
class Snapshot(val reference: DatabaseReference) : ChildEventListener {

    val snapshots = ArrayList<DataSnapshot>()

    var onDataAdded: (index: Int) -> Unit = {}
    var onDataChanged: (index: Int) -> Unit = {}
    var onDataRemoved: (index: Int) -> Unit = {}
    var onDataMoved: () -> Unit = {}

    init {
        reference.addChildEventListener(this)
    }

    override fun onChildAdded(snapshot: DataSnapshot?, previousChildKey: String?) {
        if (snapshot == null) return
        val index = 0
        snapshots.add(index, snapshot)
        onDataAdded(index)
    }

    override fun onChildChanged(snapshot: DataSnapshot?, previousChildKey: String?) {
        if (snapshot == null) return
        val index = getIndexFrom(snapshot.key)
        snapshots[index] = snapshot
        onDataChanged(index)
    }

    override fun onChildRemoved(snapshot: DataSnapshot?) {
        if (snapshot == null) return
        val index = getIndexFrom(snapshot.key)
        snapshots.removeAt(index)
        onDataRemoved(index)
    }

    override fun onChildMoved(snapshot: DataSnapshot?, previousChildKey: String?) {
        if (snapshot == null) return
        val oldIndex = getIndexFrom(snapshot.key)
        snapshots.removeAt(oldIndex)
        val newIndex = if (previousChildKey == null) 0 else getIndexFrom(previousChildKey) + 1
        snapshots.add(newIndex, snapshot)
        onDataMoved()
    }

    override fun onCancelled(snapshot: DatabaseError?) {
    }

    private fun getIndexFrom(key: String): Int {
        return snapshots.indices.filter { snapshots[it].key == key }.last()
    }

    inline fun <reified T> getValue(key: String?): T? {
        return snapshots.find { it.key == key }?.getValue(T::class.java)
    }

    inline fun <reified T> getValues(): List<T> = snapshots.map { it.getValue(T::class.java) }

}
