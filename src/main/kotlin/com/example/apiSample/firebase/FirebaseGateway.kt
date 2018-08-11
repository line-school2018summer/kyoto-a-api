package com.example.apiSample.firebase

import  com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.auth.oauth2.GoogleCredentials
import java.io.FileInputStream

class  FirebaseGateway{

    val  firebase_account_path= "src/main/resources/line-summer-kyoto-a-firebase-adminsdk-ei5yu-5a45cf67ce.json"
    val databaseUrl = "https://line-summer-yoto-a.firebaseio.com/"

    //初期化は一度だけ行えば良い
    fun authInit() {
        val firebase_account = FileInputStream(firebase_account_path)

        val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(firebase_account))
                .setDatabaseUrl(databaseUrl)
                .build()

        FirebaseApp.initializeApp(options)
    }

    /*引数
    - id_token: String : クライアントから送られてきたJWT
    戻り値
    - uid: String?: 正しいJWTの場合uidを返す。正しくない場合はnull。
    */
    fun verifyIdToken(id_token: String): String?{
        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdTokenAsync(id_token).get()
            val uid = decodedToken.getUid();
            return uid
        }
        catch(e: Exception){
            return null

        }
    }
}