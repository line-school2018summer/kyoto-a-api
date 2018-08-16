package com.example.apiSample

import org.junit.Test
import org.junit.Assert.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import com.squareup.moshi.Moshi
import com.example.apiSample.firebase.FirebaseGateway
import com.squareup.moshi.KotlinJsonAdapterFactory
import java.io.File

class FirebaseGatewayTests{

    @Test
    fun verifyIdTokenTest(){

        //初期化
        val auth = FirebaseGateway()

        val auth2 = FirebaseGateway()

        //CASE1 : jwtの形式でない場合はnullが返ってくる。例外のログが出力される。
        var token  = "hogehoge"
        assertEquals(null,auth.verifyIdToken((token)))

      //CASE2 : 有効期限切れのトークンの場合はnullが返ってくる。例外のログが出力される。
        token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImZmNTRmZjM0MTFiZmMwMDJiYTBjZDAwNzA2YmEzYmM4NTBiZWIwMmIifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbGluZS1zdW1tZXIta3lvdG8tYSIsImF1ZCI6ImxpbmUtc3VtbWVyLWt5b3RvLWEiLCJhdXRoX3RpbWUiOjE1MzM5MTUxODgsInVzZXJfaWQiOiJYNG9SUFdKclFVYnZRd0ZMekY5bDk4cGN6ZGgxIiwic3ViIjoiWDRvUlBXSnJRVWJ2UXdGTHpGOWw5OHBjemRoMSIsImlhdCI6MTUzMzkxNTE4OCwiZXhwIjoxNTMzOTE4Nzg4LCJlbWFpbCI6ImhvZ2VAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7ImVtYWlsIjpbImhvZ2VAZ21haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.U66QQ6jhWj0oh3azCVseFYsNJjwmtPB02e7W83elz91A2flwoO8nHCcoJ8oykpPIQFotKO3LZmrrP9-r0WGxxfQ-a34d7Be6HA1QfGy0_Hc0K8e_i_aObzP3gkVs8-08qbjFL2VfEmbkTh5wncMUpEAwGWCR4cWD99T3i9DzjksssYiNUXC7qHynbe_XRystZJmTWpd34HtatjXlUdS76CrSOGmxp-y9mGAlZg5DdvWQB5eefX25vd62-wZGwNUdK1YMK5FhQKaDvViUTejCqAaezhhIIcc62-TIA3YT5xcWTWM9ITKArPRP6z_pUun1_3wF532Fjq2qtyeFscO-UQ"
        assertEquals(null,auth.verifyIdToken((token)))

      // CASE3 ;:有効期限内のトークンの 場合はuidが返ってくる

        //APIキーの読み込み
        val file = File("src/main/resources/firebase_api.json")
        val fileContents = file.absoluteFile.bufferedReader().use { it.readText() }
        data class API(val apiKey: String,  val authDomain: String, val databaseURL: String, val storageBucket: String )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(API::class.java)
        val decoded_json = adapter.fromJson(fileContents) ?: API("","","","")
        val apikey = decoded_json.apiKey

        val url = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=${apikey}"
        val client: OkHttpClient = OkHttpClient.Builder().build()

        //リクエストボディの作成
        val json = JSONObject()
        json.put("email","hoge@gmail.com")
        json.put("password","hogehoge")
        json.put("returnSecureToken",true)

        //POST
        val postBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
        val request: Request = Request.Builder().url(url).post(postBody).build()
        val response = client.newCall(request).execute()
        val result: String? = response.body()?.string()

        //検証
        token = JSONObject(result).getString("idToken")
        println(token)
        assertEquals("X4oRPWJrQUbvQwFLzF9l98pczdh1",auth.verifyIdToken(token))
    }
}
