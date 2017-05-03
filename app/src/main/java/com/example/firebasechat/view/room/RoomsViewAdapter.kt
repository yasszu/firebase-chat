package com.example.firebasechat.view.room

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.firebasechat.databinding.ItemRoomBinding
import com.example.firebasechat.model.Room

/**
 * Created by Yasuhiro Suzuki on 2017/05/03.
 */
class RoomsViewAdapter(val viewModel: RoomsViewModel) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val onListChangedCallback = object : ObservableList.OnListChangedCallback<ObservableList<Room>>(){

        override fun onItemRangeInserted(p0: ObservableList<Room>?, p1: Int, p2: Int) {
            notifyItemRangeInserted(p1, p2)
        }

        override fun onItemRangeRemoved(p0: ObservableList<Room>?, p1: Int, p2: Int) {
            notifyItemRangeRemoved(p1, p2)
        }

        override fun onChanged(p0: ObservableList<Room>?) {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(p0: ObservableList<Room>?, p1: Int, p2: Int) {
            notifyItemRangeChanged(p1, p2)
        }

        override fun onItemRangeMoved(p0: ObservableList<Room>?, p1: Int, p2: Int, p3: Int) {
            notifyItemMoved(p1, p2)
        }

    }

    init {
        viewModel.addOnListChangeCallback(onListChangedCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val binding = ItemRoomBinding.inflate(inflater, parent, false)
        return RoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as RoomViewHolder
        holder.bind(viewModel.rooms[position], viewModel)
    }

    override fun getItemCount(): Int {
        return viewModel.rooms.size
    }

    class RoomViewHolder(val binding: ItemRoomBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(room: Room, viewModel: RoomsViewModel) {
            binding.room = room
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

    }

}
