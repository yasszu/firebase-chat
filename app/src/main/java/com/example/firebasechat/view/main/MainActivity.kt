package com.example.firebasechat.view.main

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.example.firebasechat.view.common.BaseActivity
import com.example.firebasechat.R
import com.example.firebasechat.view.chat.ChatActivity

class MainActivity : BaseActivity() {

    val chatRoomId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        findViewById(R.id.start_button).setOnClickListener { ChatActivity.start(this, chatRoomId) }
    }

    fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }
}
