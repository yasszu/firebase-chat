package com.example.firebasechat.view.room

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.firebasechat.R
import com.example.firebasechat.databinding.ActivityRoomsBinding
import com.example.firebasechat.view.base.BaseActivity

/**
 * Created by Yasuhiro Suzuki on 2017/05/03.
 */

class RoomsActivity : BaseActivity() {

    lateinit var binding: ActivityRoomsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rooms)
        setSupportActionBar(binding.toolbar)
        initFragment()
    }

    fun initFragment() {
        val fragment = fragmentManager.findFragmentByTag(RoomsFragment.TAG)
        if (fragment == null) {
            addFragment(RoomsFragment.newInstance(), RoomsFragment.TAG)
        }
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, RoomsActivity::class.java)
            activity.startActivity(intent)
        }
    }

}
