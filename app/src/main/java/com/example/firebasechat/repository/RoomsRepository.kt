package com.example.firebasechat.repository

import com.example.firebasechat.model.Room

/**
 * Created by Yasuhiro Suzuki on 2017/05/03.
 */
object RoomsRepository {

    fun findAll() : List<Room>  {
        // TODO Dummy data
        val rooms = listOf(
            Room(id = "0"),
            Room(id = "tsxdsdr3j9"),
            Room(id = "test1234523"),
            Room(id = "#sdvsriop"),
            Room(id = "45o9bjsdmrl")
        )
        return rooms
    }

}
