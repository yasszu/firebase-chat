package com.example.firebasechat.view.chat

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.example.firebasechat.R
import com.example.firebasechat.extension.loadCircleImage
import com.example.firebasechat.model.Member
import com.example.firebasechat.model.Message
import com.example.firebasechat.model.ViewType
import com.example.firebasechat.repository.MembersRepository
import com.example.firebasechat.repository.MessagesRepository
import com.example.firebasechat.repository.SessionRepository
import com.example.firebasechat.view.base.BaseFragment
import com.example.firebasechat.view.chat.ChatAdapter.MessageViewHolder
import com.google.firebase.database.DatabaseReference


class ChatFragment : BaseFragment() {

    val roomId: Int by lazy { arguments.getInt(ROOM_ID, 0) }

    val members: MembersRepository by lazy { MembersRepository(roomId) }

    val messages: MessagesRepository by lazy { MessagesRepository(roomId) }

    val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply {
            stackFromEnd = true
        }
    }

   val adapter: ChatAdapter by lazy {
       ChatAdapter(messages.reference, object : ChatAdapter.Listener {
           override fun onGetItemViewType(message: Message): Int {
               return if (SessionRepository.isSelf(message)) ViewType.ME.id else ViewType.OTHERS.id
           }

           override fun onPopulate(viewHolder: MessageViewHolder?, message: Message?, ref: DatabaseReference) {
               populateItems(viewHolder!!, message!!, ref)
           }
       }).apply {
           registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
               override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                   super.onItemRangeInserted(positionStart, itemCount)
                   scrollToShowNewItem(positionStart)
               }
           })
       }
   }

    lateinit var recyclerView: RecyclerView

    lateinit var editText: EditText

    lateinit var sendButton: View

    companion object {
        val ROOM_ID = "roomId"
        val TAG: String = ChatFragment::class.java.simpleName
        fun newInstance(matchingId: Int): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putInt(ROOM_ID, matchingId)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_chat, container, false)
        bindViews(view)
        sendProfile()
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    fun bindViews(view: View?) {
        recyclerView = view?.findViewById(R.id.recyclerView) as RecyclerView
        editText = view.findViewById(R.id.editText) as EditText
        sendButton = view.findViewById(R.id.sendButton).apply {
            setOnClickListener { writeMessage(editText.text.toString()) }
        }
    }

    fun sendProfile() {
        val currentUser = SessionRepository.currentUser?: return
        members.write(currentUser)
    }

    fun initRecyclerView() {
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    fun populateItems(viewHolder: MessageViewHolder, message: Message, ref: DatabaseReference) {
        val member: Member? = members.find(message.uid)
        viewHolder.content.text = message.body
        viewHolder.content.isSelected = message.saved
        viewHolder.content.setTextColor(if (message.saved) Color.WHITE else Color.BLACK)
        viewHolder.author.text = member?.name
        setThumbnail(viewHolder.thumbnail, member?.photoUrl)
        viewHolder.content.setOnClickListener {
            ref.setValue(Message(message.uid, message.body, message.timestamp, !message.saved))
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
        imageView?.loadCircleImage(url, R.drawable.ic_account_circle)
    }

    fun writeMessage(text: String) {
        val uid = SessionRepository.uid ?: return
        messages.post(Message(uid, text, messages.timeStamp, false))
        editText.setText("")
    }

}