package com.example.apiSample.controller

import com.example.apiSample.firebase.AuthGateway
import com.example.apiSample.model.NonUidUser
import com.example.apiSample.service.UserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
@Api(value = "api",description = "ユーザーに関するAPIです。")
class UserController(private val userService: UserService,
                     private val auth: AuthGateway) {

    @ApiOperation(value = "ユーザーを作成します。")
    @PostMapping(
            value = ["/users"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun createUser(@RequestHeader(value="Token", required=true)token: String,
                   @RequestParam("name") name: String): NonUidUser{
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
        return userService.create(uid, name)
    }

    @ApiOperation(value = "ユーザーの一覧を取得します。")
    @GetMapping(
            value = ["/users"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getUserList(): ArrayList<NonUidUser>{
        return userService.getUserList()
    }

    @ApiOperation(value = "ログインしているユーザーの情報を取得します。")
    @GetMapping(
            value = ["/users/me"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun getUserInfoById(@RequestHeader(value="Token", required=true)token: String): NonUidUser {
        val uid = auth.verifyIdToken(token) ?: throw UnauthorizedException("Your token is invalid.")
        val id = userService.findByUid(uid).id
        return userService.findById(id)
    }

    @ApiOperation(value = "ログインしているユーザーの情報を変更します。")
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

    @ApiOperation(value = "idに対応したユーザーの情報を取得します。")
    @GetMapping(
            value = ["/users/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun findById(@PathVariable("id")id: Long): NonUidUser{
        return userService.findById(id)
    }
}
