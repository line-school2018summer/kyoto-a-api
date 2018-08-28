package com.example.apiSample.firebase

interface AuthGateway{
    fun verifyIdToken(id_token: String?): String?
}