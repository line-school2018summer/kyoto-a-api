package com.example.apiSample.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.sql.Timestamp
import java.util.Comparator


data class User(
        var id: Long,
        var uid: String,
        var name: String,
        @get:JsonProperty("created_at") var createdAt: Timestamp,
        @get:JsonProperty("updated_at") var updatedAt: Timestamp
)

data class UserList(

        @ApiModelProperty(example="1", position=0)
        var id: Long,

        @ApiModelProperty(example="山田", position=1)
        var name: String
)

data class IconUser(
    var id: Long,
    var name: String,
    var icon: String?,
    @get:JsonProperty("created_at") var createdAt: Timestamp,
    @get:JsonProperty("updated_at") var updatedAt: Timestamp
)

data class Room(

        @ApiModelProperty(example="1", position=0)
        var id: Long,

        @ApiModelProperty(example="海外旅行ぐる", position=1)
        var name: String,

        @ApiModelProperty(position=2,example = "こんにちは")
        var last_message_text: String?,

        @ApiModelProperty(position=3, example="2018-08-24T00:00:00.000+0000")
        @get:JsonProperty("last_message_created_at") var last_message_created_at: Timestamp?,

        @ApiModelProperty(example="2018-08-24T00:00:00.000+0000",position=4)
        @get:JsonProperty("created_at") var createdAt: Timestamp,

        @ApiModelProperty(example="2018-08-24T00:00:00.000+0000",position=5)
        @get:JsonProperty("updated_at") var updatedAt: Timestamp
)

data class RoomList(
        var id: Long,
        var name: String
)

data class UserRoom(
        var userId: Long,
        var roomId: Long
)

data class NonUidUser(
        @ApiModelProperty(example="1", position=0)
        var id: Long,

        @ApiModelProperty(example="山田",position=1)
        var name: String,

        @ApiModelProperty(example="2018-08-24T00:00:00.000+0000",position=3)
        @get:JsonProperty("created_at") var createdAt: Timestamp,

        @ApiModelProperty(example="2018-08-24T00:00:00.000+0000",position=4)
        @get:JsonProperty("updated_at") var updatedAt: Timestamp
)


data class Message(
        @ApiModelProperty(example="1", position=0)
        var id: Long,

        @ApiModelProperty(example="1", position=1)
        var room_id: Long,

        @ApiModelProperty(example="1", position=2)
        var user_id: Long,

        @ApiModelProperty(example="こんにちは", position=3)
        var text: String,

        @ApiModelProperty(example="太郎",position=4)
        var user_name: String,

        @ApiModelProperty(example="2018-08-24T00:00:00.000+0000",position=5)
        @get:JsonProperty("created_at") var createdAt: Timestamp,

        @ApiModelProperty(example="2018-08-24T00:00:00.000+0000",position=6)
        @get:JsonProperty("updated_at") var updatedAt: Timestamp
)


data class MessageForMapping (

        @ApiModelProperty(example="1", position=0)
        var message_id: Long,

        @ApiModelProperty(example="1", position=1)
        var room_id: Long,

        @ApiModelProperty(example="1", position=2)
        var user_id: Long,

        @ApiModelProperty(example="こんにちは", position=3)
        var text: String,

        @ApiModelProperty(example="2018-08-24T00:00:00.000+0000",position=4)
        var message_created_at: Timestamp,

        @ApiModelProperty(example="2018-08-24T00:00:00.000+0000",position=5)
        var message_updated_at: Timestamp,

        @ApiModelProperty(example="山田", position=6)
        var user_name: String,

        @ApiModelProperty(example="2018-08-24T00:00:00.000+0000",position=7)
        var user_created_at: Timestamp,

        @ApiModelProperty(example="2018-08-24T00:00:00.000+0000",position=8)
        var user_updated_at: Timestamp
)

data class RoomForMapping (
        var room_id: Long,
        var message_id: Long?,
        var room_name: String,
        var message_user_id: Long?,
        var message_text: String?,
        var message_created_at: Timestamp?,
        var message_updated_at: Timestamp?,
        var room_created_at: Timestamp,
        var room_updated_at: Timestamp
)

class RoomComparator : Comparator<Room> {

        override fun compare(R1: Room, R2: Room): Int {
                return if (lastActivityTime(R1) < lastActivityTime((R2))) 1 else -1
        }

        fun lastActivityTime(room: Room): Timestamp {
                val last_message_created_at: Timestamp? = room.last_message_created_at
                if (last_message_created_at == null) {
                        return room.createdAt
                } else {
                        return last_message_created_at
                }
        }
}

data class Event(
    var id: Long,
    var event_type: Int,
    var room_id: Long?,
    var user_id: Long?,
    var message_id: Long?
)

data class InsertEvent(
    var id: Long = 0,
    var event_type: Int,
    var room_id: Long?,
    var user_id: Long?,
    var message_id: Long?
)

enum class EventTypes {
    PROFILE_UPDATED,
    ROOM_CREATED,
    ROOM_UPDATED,
    ROOM_MEMBER_JOINED,
    ROOM_MEMBER_LEAVED,
    ROOM_MEMBER_DELETED,
    MESSAGE_SENT,
    MESSAGE_UPDATED,
    MESSAGE_DELETED,
}
