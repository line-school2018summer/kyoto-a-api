package com.example.apiSample.controller

import com.example.apiSample.model.User
import com.example.apiSample.model.UserProfile
import com.example.apiSample.model.UserList
import com.example.apiSample.service.UserProfileService
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

data class UserListResponse(
        var id: Long,
        var name: String,
        var email: String
)

data class PostSearchRequest(
        val search_str: String
)

@RestController
class UserController(private val userProfileService: UserProfileService,
                     private val userService: UserService, private,
                     private val auth: FirebaseGateway) {

    @GetMapping(
            value = ["/users"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getUserList(): ArrayList<User>{
        return userService.getUserList()
    }

    @GetMapping(
            value = ["/users/me"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getUserInfoById(@RequestHeader(value="Token", required=true)token: String): User {
        auth.authInit()
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
        return userService.findByUid(uid)
    }

    @PutMapping(
            value = ["/users/me"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun updateName(@RequestHeader(value="Token", required=true)token: String,
                   @PathParam("name") changedName: String): Unit {
        auth.authInit()
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
        userService.updateName(uid, changedName)
    }

    @GetMapping(
            value = ["/users/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun findById(@PathVariable("id")id: Long): User{
        return userService.findById(id)
    }

    //まだ実装していません（apiSampleのまま）
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
