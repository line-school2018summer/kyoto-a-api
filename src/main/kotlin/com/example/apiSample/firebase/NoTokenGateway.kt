package com.example.apiSample.firebase

import org.springframework.stereotype.Component



//@Component
class  NoTokenGateway : AuthGateway{


    /*引数
    - id_token: String : クライアントから送られてきたJWT
    戻り値
    - uid: String?: 必ず"fakeid1"を返す
    */
    override fun verifyIdToken(id_token: String?): String?{
        return "fakeid1"
    }
}
