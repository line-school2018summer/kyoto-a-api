package com.example.apiSample.service

import com.example.apiSample.mapper.UserMapper
import com.example.apiSample.model.UserProfile
import org.springframework.stereotype.Service

@Service
class UserProfileService(private val userMapper: UserMapper) {
    fun getProfile(userId: Long): UserProfile {
        val profile = userMapper.findByUserId(userId)
        return profile
    }
}
