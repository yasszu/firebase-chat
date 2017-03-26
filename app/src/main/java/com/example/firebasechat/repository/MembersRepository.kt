package com.example.firebasechat.repository

import android.util.Log
import com.example.firebasechat.model.Member
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Yasuhiro Suzuki on 2017/03/20.
 */
class MembersRepository(val roomId: Int) {

    val MEMBERS_CHILD = "members"

    val reference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance()
                .getReference(MEMBERS_CHILD)
                .child(roomId.toString())
    }

    val firebaseData: FirebaseData

    init {
        firebaseData = FirebaseData(reference)
    }

    fun write(currentUser: FirebaseUser) {
        val uid = currentUser.uid
        Log.d("name", currentUser.displayName)
        val name = currentUser.displayName
        val photoUrl = currentUser.photoUrl.toString()
        val member = Member(name, photoUrl)
        write(uid, member)
    }

    fun write(uid: String, member: Member) {
        if (uid.isEmpty()) return
        reference.child(uid).setValue(member)
    }

    fun find(uid: String?): Member? {
        return firebaseData.snapshots.find { it.key == uid }?.getValue(Member::class.java)
    }

    fun findAll(): List<Member> = firebaseData.snapshots.map { it.getValue(Member::class.java) }

}