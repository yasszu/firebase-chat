package com.example.firebasechat.view.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.firebasechat.R
import com.example.firebasechat.view.base.BaseActivity

class ChatActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setToolbar()
        val matchingId = intent.getIntExtra(CHAT_ROOM_ID, -1)
        initFragment(matchingId)
    }

    private fun setToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    fun initFragment(matchingId: Int) {
        supportFragmentManager.findFragmentByTag(ChatFragment.TAG).let {
            addFragment(ChatFragment.newInstance(matchingId), ChatFragment.TAG)
        }
    }

    companion object {
        val CHAT_ROOM_ID = "chatRoomId"
        fun start(activity: Activity, matchingId: Int) {
            val intent = Intent(activity, ChatActivity::class.java)
            intent.putExtra(CHAT_ROOM_ID, matchingId)
            activity.startActivity(intent)
        }
    }

}