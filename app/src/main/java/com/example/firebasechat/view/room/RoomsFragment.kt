package com.example.firebasechat.view.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebasechat.databinding.FragmentRoomsBinding
import com.example.firebasechat.view.base.BaseFragment

/**
 * Created by Yasuhiro Suzuki on 2017/05/03.
 */
class RoomsFragment : BaseFragment() {

    lateinit var binding: FragmentRoomsBinding

    companion object {
        val TAG = RoomsFragment::class.java.simpleName!!
        fun newInstance(): RoomsFragment = RoomsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRoomsBinding.inflate(inflater, container, false)
        return binding.root
    }

}
