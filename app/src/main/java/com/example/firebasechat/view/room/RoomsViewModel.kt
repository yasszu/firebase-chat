package com.example.firebasechat.view.room

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.view.View
import com.example.firebasechat.model.Room
import com.example.firebasechat.repository.RoomsRepository

/**
 * Created by Yasuhiro Suzuki on 2017/05/03.
 */
class RoomsViewModel {

    var onClickItem: (id: String) -> Unit = {}

    var onClickSubmit: () -> Unit = {}

    val rooms: ObservableArrayList<Room> = ObservableArrayList()

    var callback: ObservableList.OnListChangedCallback<ObservableList<Room>>? = null

    init {
        setDataChangeListener()
    }

    fun setDataChangeListener() {
        RoomsRepository.onSnapshotsUpdate = {
            fetch()
        }
    }

    fun fetch() {
        this.rooms.clear()
        setRooms(RoomsRepository.findAll())
    }

    fun setRooms(rooms: List<Room>?) {
        rooms?.map { this.rooms.add(it) }
    }

    fun addOnListChangeCallback(callback: ObservableList.OnListChangedCallback<ObservableList<Room>>) {
        this.callback = callback
        rooms.addOnListChangedCallback(callback)
    }

    fun onClickItem(room: Room) {
        onClickItem(room.id)
    }

    fun onClickSubmit(view: View) {
        onClickSubmit()
    }

    fun destroy() {
        rooms.removeOnListChangedCallback(callback)
        callback = null
        RoomsRepository.removeListener()
    }

}
