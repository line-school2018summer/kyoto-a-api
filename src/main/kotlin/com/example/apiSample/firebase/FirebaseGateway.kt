package com.example.apiSample.firebase

import  com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.auth.oauth2.GoogleCredentials
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory

interface AuthGateway{
    fun verifyIdToken(id_token: String): String?
}

@Component
class  FirebaseGateway : AuthGateway{

    val logger = LoggerFactory.getLogger(FirebaseGateway::class.java)

    companion object {
        init {
            val firebase_account_path = "/line-summer-kyoto-a-firebase-adminsdk-ei5yu-5a45cf67ce.json"
            val databaseUrl = "https://line-summer-yoto-a.firebaseio.com/"

            val firebase_account = FirebaseGateway::class.java.getResourceAsStream(firebase_account_path)

            val options = FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(firebase_account))
                    .setDatabaseUrl(databaseUrl)
                    .build()

            FirebaseApp.initializeApp(options)
        }
    }

    /*引数
    - id_token: String : クライアントから送られてきたJWT
    戻り値
    - uid: String?: 正しいJWTの場合uidを返す。正しくない場合はnull。
    */
    override fun verifyIdToken(id_token: String): String?{

        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdTokenAsync(id_token).get()
            val uid = decodedToken.getUid();
            return uid
        }
        catch(e: Exception){
            logger.info("error",e)
            return null

        }
    }
}
