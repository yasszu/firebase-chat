package com.example.firebasechat.model

import com.example.firebasechat.R

/**
 * Created by Yasuhiro Suzuki on 2017/07/23.
 */
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