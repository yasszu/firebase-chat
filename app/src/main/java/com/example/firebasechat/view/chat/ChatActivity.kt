package com.example.firebasechat.view.chat

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.example.firebasechat.R
import com.example.firebasechat.databinding.ActivityChatBinding
import com.example.firebasechat.view.base.BaseActivity

class ChatActivity : BaseActivity() {

    val matchingId: String by lazy { intent.getStringExtra(CHAT_ROOM_ID) }

    lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        initToolbar()
        initFragment(matchingId)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun initFragment(roomId: String) {
        if (supportFragmentManager.findFragmentByTag(ChatFragment.TAG) == null) {
            addFragment(ChatFragment.newInstance(roomId), ChatFragment.TAG)
        }
    }

    companion object {
        val CHAT_ROOM_ID = "chatRoomId"
        fun start(activity: Activity, matchingId: String) {
            val intent = Intent(activity, ChatActivity::class.java)
            intent.putExtra(CHAT_ROOM_ID, matchingId)
            activity.startActivity(intent)
        }
    }

}
