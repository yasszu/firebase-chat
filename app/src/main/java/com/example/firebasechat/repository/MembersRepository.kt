package com.example.firebasechat.repository

import com.example.firebasechat.model.Member
import com.example.firebasechat.model.Snapshot
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Yasuhiro Suzuki on 2017/03/20.
 */
class MembersRepository(roomId: String) {

    val MEMBERS_CHILD = "members"

    val reference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference(MEMBERS_CHILD)
        .child(roomId)

    val snapshot: Snapshot

    init {
        snapshot = Snapshot(reference)
    }

    fun write(currentUser: FirebaseUser) {
        val uid = currentUser.uid
        val name = currentUser.displayName
        val photoUrl = currentUser.photoUrl.toString()
        val member = Member(name, photoUrl)
        write(uid, member)
    }

    fun write(uid: String, member: Member) {
        if (uid.isEmpty()) return
        reference.child(uid).setValue(member)
    }

    fun find(uid: String?): Member? = snapshot.getValue(uid)

    fun findAll(): List<Member> = snapshot.getValues()

}
