package com.example.firebasechat.view.chat

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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
import com.example.firebasechat.repository.MessagesRepository
import com.example.firebasechat.view.chat.ChatAdapter.MessageViewHolder
import com.example.firebasechat.view.chat.ChatAdapter.ViewType
import com.example.firebasechat.view.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class ChatFragment : BaseFragment() {

    val roomId: Int
        get() = arguments.getInt(ROOM_ID, 0)

    var me: Member = Member()
    var members: MutableMap<String, Member> = mutableMapOf()

    lateinit var messages: MessagesRepository
    lateinit var auth: FirebaseAuth
    lateinit var adapter: ChatAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var editText: EditText
    lateinit var sendButton: Button

    companion object {
        val ROOM_ID = "roomId"
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
        auth = FirebaseAuth.getInstance()
        messages = MessagesRepository(roomId)
        bindViews(view)
        fetchProfile()
        fetchMembers()
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSendButton()
    }

    fun bindViews(view: View?) {
        recyclerView = view?.findViewById(R.id.recyclerView) as RecyclerView
        editText = view.findViewById(R.id.editText) as EditText
        sendButton = view.findViewById(R.id.sendButton) as Button
    }

    fun fetchMembers() {
        val members = listOf<Member>()
        this.members.put(me.id, me)
        setMembers(members)
        initRecyclerView()
    }

    fun fetchProfile() {
        if (auth.currentUser != null) {
            val name = auth.currentUser?.displayName.let { "name" }
            val url = auth.currentUser?.photoUrl.let { "" }
            val uid = auth.currentUser?.uid.let { "" }
            me = Member(uid, name, url)
        } else {
            me = Member("","","")
        }
    }

    fun setMembers(members: List<Member>) {
        members.map { this.members.put(it.id, it) }
    }

    fun initRecyclerView() {
        initAdapter()
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    fun initAdapter() {
        adapter = ChatAdapter(messages.reference, object : ChatAdapter.Listener {
            override fun onGetItemViewType(message: Message): Int {
                return if (isSelf(message.userId)) ViewType.ME.id else ViewType.OTHERS.id
            }

            override fun onPopulate(viewHolder: MessageViewHolder?, message: Message?, ref: DatabaseReference) {
                populateItems(viewHolder!!, message!!, ref)
            }
        })
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                scrollToShowNewItem(positionStart)
            }
        })
    }

    fun populateItems(viewHolder: MessageViewHolder, message: Message, ref: DatabaseReference) {
        val member = members[message.userId]
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
        val MessageCount = adapter.itemCount
        val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
        if (lastVisiblePosition == -1 || positionStart >= MessageCount - 1 && lastVisiblePosition == positionStart - 1) {
            recyclerView.scrollToPosition(positionStart)
        }
    }

    private fun setThumbnail(imageView: ImageView?, url: String?) {
        if (url == null || url.isBlank()) return
        imageView?.loadCircleImage(url, R.drawable.ic_account_circle)
    }

    fun initSendButton(){
        sendButton.setOnClickListener {
            writeMessage(editText.text.toString())
        }
    }

    fun writeMessage(text: String) {
        messages.write(Message(me.id, text, timeStamp, false))
        editText.setText("")
        messages.findAll().map { Log.d("message", it.body) }
    }

    val timeStamp: Long
        get() = System.currentTimeMillis()

    fun isSelf(userId: String) = me.id == userId

}