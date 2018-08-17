package com.example.apiSample

import org.junit.Test
import org.junit.Assert.*

import com.example.apiSample.firebase.NoTokenGateway

class NoTokenGatewayTests{

    @Test
    fun verifyIdTokenTest(){

        //初期化
        val auth = NoTokenGateway()

        //CASE1 : nullでも、fakeid1が返ってくる
        var token: String?  = null
        assertEquals("fakeid1",auth.verifyIdToken((token)))

      //CASE2 : 有効期限切れのトークンの場合でもfakeid1が返ってくる
        token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImZmNTRmZjM0MTFiZmMwMDJiYTBjZDAwNzA2YmEzYmM4NTBiZWIwMmIifQ.U66QQ6jhWj0oh3azCVseFYsNJjwmtPB02e7W83elz91A2flwoO8nHCcoJ8oykpPIQFotKO3LZmrrP9-r0WGxxfQ-a34d7Be6HA1QfGy0_Hc0K8e_i_aObzP3gkVs8-08qbjFL2VfEmbkTh5wncMUpEAwGWCR4cWD99T3i9DzjksssYiNUXC7qHynbe_XRystZJmTWpd34HtatjXlUdS76CrSOGmxp-y9mGAlZg5DdvWQB5eefX25vd62-wZGwNUdK1YMK5FhQKaDvViUTejCqAaezhhIIcc62-TIA3YT5xcWTWM9ITKArPRP6z_pUun1_3wF532Fjq2qtyeFscO-UQ"
        assertEquals("fakeid1",auth.verifyIdToken((token)))

    }
}
