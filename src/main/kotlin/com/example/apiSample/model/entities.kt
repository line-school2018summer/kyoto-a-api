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

data class UserList(
        var id: Long,
        var name: String,
        var email: String
)

data class Talk(
        var id: Long,
        var room_id: Long,
        var user_id: Long,
        var text: String,
        @get:JsonProperty("created_at") var createdAt: Timestamp,
        @get:JsonProperty("updated_at") var updatedAt: Timestamp
        )

data class TalkList(
        var id: Long,
        var room_id: Long,
        var user_id: Long,
        var text: String
)
