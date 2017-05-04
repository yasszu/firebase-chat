package com.example.firebasechat.view.room

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebasechat.databinding.FragmentRoomsBinding
import com.example.firebasechat.model.Room
import com.example.firebasechat.repository.RoomsRepository
import com.example.firebasechat.view.base.BaseFragment
import com.example.firebasechat.view.chat.ChatActivity

/**
 * Created by Yasuhiro Suzuki on 2017/05/03.
 */
class RoomsFragment : BaseFragment() {

    lateinit var binding: FragmentRoomsBinding

    val viewModel: RoomsViewModel by lazy {
        RoomsViewModel().apply {
            onClickItem = { ChatActivity.start(activity, it) }
            onClickSubmit = { createRoom(binding.editText.text.toString()) }
        }
    }

    companion object {
        val TAG = RoomsFragment::class.java.simpleName!!
        fun newInstance(): RoomsFragment = RoomsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRoomsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        initRecyclerView()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroy()
    }

    fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = RoomsViewAdapter(viewModel)
        binding.recyclerView.layoutManager = layoutManager
    }

    fun createRoom(roomId: String) {
        val room = Room(id = roomId.trim())
        RoomsRepository.post(room)
    }

}
