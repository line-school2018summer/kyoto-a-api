package com.example.apiSample.controller

import com.example.apiSample.model.UserProfile
import com.example.apiSample.model.UserList
import com.example.apiSample.service.UserProfileService
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

data class UserListResponse(
        var id: Long,
        var name: String,
        var email: String
)

data class PostSearchRequest(
        val search_str: String
)

@RestController
class UserController(private val userProfileService: UserProfileService, private val userService: UserService) {
    @GetMapping(
            value = ["/user"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun hello(): String{
        return "{\"greeting\": \"こんにちは!\"}"
    }

    @GetMapping(
            value = ["/user/{id}/profile"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getProfile(@PathVariable("id" ) userId: Long): UserProfile {
        return userProfileService.getProfile(userId)
    }

    @PostMapping(
            value = ["/user/search"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getList(@RequestBody request: PostSearchRequest): Map<String, List<UserListResponse>> {
        val userList: ArrayList<UserList> = userService.findUsersList(request.search_str)
        return mapOf("results" to userList.map {
            UserListResponse(
                    id = it.id,
                    name = it.name,
                    email = it.email
            )
        })
    }

}
