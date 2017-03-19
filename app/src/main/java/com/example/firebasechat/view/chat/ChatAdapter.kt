package com.example.firebasechat.view.chat

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.firebasechat.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.example.firebasechat.model.Message

class ChatAdapter : FirebaseRecyclerAdapter<Message, ChatAdapter.MessageViewHolder> {

    enum class ViewType(val id: Int) {
        OTHERS(0) {
            override fun getLayout(): Int {
                return R.layout.item_message
            }
        },
        ME(1) {
            override fun getLayout(): Int {
                return R.layout.item_message_me
            }
        };
        abstract fun getLayout(): Int
    }

    interface Listener {
        fun onPopulate(viewHolder: MessageViewHolder?, message: Message?, ref: DatabaseReference)
        fun onGetItemViewType(message: Message): Int
    }

    lateinit var mListener: Listener

    constructor(modelClass: Class<Message>?, modelLayout: Int, viewHolderClass: Class<MessageViewHolder>?, ref: Query?) : super(modelClass, modelLayout, viewHolderClass, ref)

    constructor(modelClass: Class<Message>?, modelLayout: Int, viewHolderClass: Class<MessageViewHolder>?, ref: DatabaseReference?) : super(modelClass, modelLayout, viewHolderClass, ref)

    constructor(ref: DatabaseReference?, listener: Listener) : super(Message::class.java, R.layout.item_message, MessageViewHolder::class.java, ref) {
        this.mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageViewHolder {
        mModelLayout = when (viewType) {
            0 -> ViewType.OTHERS.getLayout()
            1 -> ViewType.ME.getLayout()
            else -> ViewType.OTHERS.getLayout()
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun populateViewHolder(viewHolder: MessageViewHolder?, message: Message?, position: Int) {
        mListener.onPopulate(viewHolder, message, getRef(position))
    }

    override fun getItemViewType(position: Int): Int {
        return mListener.onGetItemViewType(getItem(position))
    }

    class MessageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var thumbnail: ImageView = v.findViewById(R.id.thumbnail) as ImageView
        var content: TextView = v.findViewById(R.id.content) as TextView
        var author: TextView = v.findViewById(R.id.author) as TextView
    }

}
