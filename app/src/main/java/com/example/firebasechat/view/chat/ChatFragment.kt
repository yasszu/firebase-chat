package com.example.firebasechat.view.chat

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.firebasechat.R
import com.example.firebasechat.helper.ImageViewHelper.loadCircleImage
import com.example.firebasechat.model.Member
import com.example.firebasechat.model.Message
import com.example.firebasechat.view.chat.ChatAdapter.MessageViewHolder
import com.example.firebasechat.view.chat.ChatAdapter.ViewType
import com.example.firebasechat.view.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ChatFragment : BaseFragment() {

    private var mRoomId: Int = 0
    private var mMe: Member = Member()
    private var mMembers: MutableMap<String, Member> = mutableMapOf()

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mMessagesRef: DatabaseReference
    private lateinit var mAdapter: ChatAdapter
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mEditText: EditText
    private lateinit var mSendButton: Button

    companion object {
        val ROOM_ID = "roomId"
        val MESSAGES_CHILD = "messages"
        val TAG: String = ChatFragment::class.java.simpleName
        fun newInstance(matchingId: Int): ChatFragment {
            val args: Bundle = Bundle()
            val f = ChatFragment()
            args.putInt(ROOM_ID, matchingId)
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_chat, container, false)
        mRoomId = arguments.getInt(ROOM_ID, 0)
        mMessagesRef = FirebaseDatabase.getInstance().getReference(MESSAGES_CHILD).child(mRoomId.toString())
        mAuth = FirebaseAuth.getInstance()
        bindViews(view)
        fetchProfile()
        fetchMembers(mRoomId)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSendButton()
    }

    fun bindViews(view: View?) {
        mRecyclerView = view?.findViewById(R.id.recyclerView) as RecyclerView
        mEditText = view.findViewById(R.id.editText) as EditText
        mSendButton = view.findViewById(R.id.sendButton) as Button
    }

    fun fetchMembers(roomId: Int) {
        val members = listOf<Member>()
        mMembers.put(mMe.id, mMe)
        setMembers(members)
        initRecyclerView()
    }

    fun fetchProfile() {
        if (mAuth.currentUser != null) {
            val name = mAuth.currentUser?.displayName.let { "name" }
            val url = mAuth.currentUser?.photoUrl.let { "" }
            val uid = mAuth.currentUser?.uid.let { "" }
            mMe = Member(uid, name, url)
        } else {
            mMe = Member("","","")
        }
    }

    fun setMembers(members: List<Member>) {
        members.map { mMembers.put(it.id, it) }
    }

    fun initRecyclerView() {
        initAdapter()
        mLinearLayoutManager = LinearLayoutManager(context)
        mLinearLayoutManager.stackFromEnd = true
        mRecyclerView.layoutManager = mLinearLayoutManager
        mRecyclerView.adapter = mAdapter
    }

    fun initAdapter() {
        mAdapter = ChatAdapter(mMessagesRef, object : ChatAdapter.Listener {
            override fun onGetItemViewType(message: Message): Int {
                return if (isSelf(message.userId)) ViewType.ME.id else ViewType.OTHERS.id
            }

            override fun onPopulate(viewHolder: MessageViewHolder?, message: Message?, ref: DatabaseReference) {
                populateItems(viewHolder!!, message!!, ref)
            }
        })
        mAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                scrollToShowNewItem(positionStart)
            }
        })
    }

    fun populateItems(viewHolder: MessageViewHolder, message: Message, ref: DatabaseReference) {
        val member = mMembers[message.userId]
        viewHolder.content.text = message.body
        viewHolder.content.isSelected = message.saved
        viewHolder.content.setTextColor(if (message.saved) Color.WHITE else Color.BLACK)
        viewHolder.author.text = member?.name
        setThumbnail(viewHolder.thumbnail, member?.thumb)
        viewHolder.content.setOnClickListener {
            ref.setValue(Message(message.userId, message.body, message.timestamp, !message.saved))
        }
    }

    fun scrollToShowNewItem(positionStart: Int) {
        val MessageCount = mAdapter.itemCount
        val lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition()
        if (lastVisiblePosition == -1 || positionStart >= MessageCount - 1 && lastVisiblePosition == positionStart - 1) {
            mRecyclerView.scrollToPosition(positionStart)
        }
    }

    private fun setThumbnail(imageView: ImageView?, url: String?) {
        if (url == null || url.isBlank()) return
        imageView?.loadCircleImage(url, R.drawable.ic_account_circle)
    }

    fun initSendButton(){
        mSendButton.setOnClickListener {
            writeMessage(mEditText.text.toString())
        }
    }

    fun writeMessage(text: String) {
        val message = Message(mMe.id, text, timeStamp, false)
        mMessagesRef.push().setValue(message)
        mEditText.setText("")
    }

    val timeStamp: Long
        get() = System.currentTimeMillis()

    fun isSelf(userId: String) = mMe.id.equals(userId)

}