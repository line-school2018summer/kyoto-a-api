package com.example.apiSample.controller

import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.model.NonUidUser
import com.example.apiSample.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


data class UserListResponse(
        var id: Long,
        var uid: String,
        var name: String
)

data class PostSearchRequest(
        val search_str: String
)

@RestController
class UserController(private val userService: UserService,
                     private val auth: AuthGateway) {

    @PostMapping(
            value = ["/users"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createUser(@RequestHeader(value="Token", required=true)token: String,
                   @RequestParam("name") name: String): NonUidUser{
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
        return userService.create(uid, name)
    }

    @GetMapping(
            value = ["/users"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getUserList(): ArrayList<NonUidUser>{
        return userService.getUserList()
    }

    @GetMapping(
            value = ["/users/me"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getUserInfoById(@RequestHeader(value="Token", required=true)token: String): NonUidUser {
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
        val id = userService.findByUid(uid).id
        return userService.findById(id)
    }

    @PutMapping(
            value = ["/users/me"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun updateName(@RequestHeader(value="Token", required=true)token: String,
                   @RequestParam("name") changedName: String): NonUidUser {
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
        val id = userService.findByUid(uid).id
        return userService.updateName(id, changedName)
    }

    @GetMapping(
            value = ["/users/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun findById(@PathVariable("id")id: Long): NonUidUser{
        return userService.findById(id)
    }

    @GetMapping(
        value = ["/users/search"],
        produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun searchUser(@RequestParam("name") searchStr: String): List<NonUidUser>{
        return userService.searchUser(searchStr)
    }
}
