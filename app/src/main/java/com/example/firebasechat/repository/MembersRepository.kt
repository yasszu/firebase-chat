package com.example.firebasechat.repository

import com.example.firebasechat.model.Member
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Yasuhiro Suzuki on 2017/03/20.
 */
class MembersRepository(val roomId: Int) {

    val MEMBERS_CHILD = "members"

    val reference: DatabaseReference
        get() = FirebaseDatabase.getInstance()
                .getReference(MEMBERS_CHILD)
                .child(roomId.toString())

    val firebaseData: FirebaseData

    init {
        firebaseData = FirebaseData(reference)
    }

    fun write(userId: String, member: Member) {
        if (userId.isEmpty()) return
        reference.push().setValue(member)
    }

    /** Get current snapshots **/
    fun findAll(): List<Member> = firebaseData.snapshots.map { it.getValue(Member::class.java) }

}