package com.example.apiSample.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

data class UserProfile(
    var id: Long,
    var name: String,
    var email: String,
    @get:JsonProperty("created_at") var createdAt: Timestamp,
    @get:JsonProperty("updated_at") var updatedAt: Timestamp
)

data class User(
        var id: Long,
        var uid: String,
        var name: String,
        @get:JsonProperty("created_at") var createdAt: Timestamp,
        @get:JsonProperty("updated_at") var updatedAt: Timestamp
)

data class UserList(
        var id: Long,
        var uid: String,
        var name: String
)

data class Room(
        var id: Long,
        var name: String,
        @get:JsonProperty("created_at") var createdAt: Timestamp,
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