package com.example.scadatechnicaltirtanadi.presenter

import android.content.Context
import android.util.Log
import com.example.apps_magang.utils.ResultState
import com.example.mateup.view.user_view
import com.example.scadatechnicaltirtanadi.data.AccessToken
import com.example.scadatechnicaltirtanadi.remote.AuthApi
import com.example.scadatechnicaltirtanadi.remote.SignInRequest
import io.realm.Realm
import io.realm.RealmList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class UserData(
    private val authApi: AuthApi,
    private val view: user_view,
) {
    fun getUser(
        username: String, password: String
    ) {
        val signInRequest = SignInRequest(
            username = username,
            password = password,
            grantType = "password",
            clientSecret = "1q2w3e4r",
            clientId = "bas-web-admin"
        )

        val call = authApi.signIn(signInRequest)
        call.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    tokenResponse?.let {
                        saveToken(tokenResponse)
                        view.onLogin(ResultState.Success(tokenResponse))
                        Log.d("Token", "Response: $response")
                    }
                } else {
                    Log.e("Token", "Error: ${response.message()}")
                    view.onLogin(ResultState.Error(response.message()))
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                Log.e("Token", "Error: ${t.message}")
                view.onLogin(ResultState.Error(t.message.toString()))
            }
        })
    }

    private fun saveToken(dataItem: AccessToken) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { backgroundRealm ->
            backgroundRealm.insertOrUpdate(dataItem)
        }
        realm.close()
    }

    fun getAccessToken(): String? {
        val realm = Realm.getDefaultInstance()
        val token = realm.where(AccessToken::class.java).findFirst()
        val accessToken = token?.accessToken
        realm.close()
        return accessToken
    }

    fun deleteToken() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { backgroundRealm ->
            backgroundRealm.where(AccessToken::class.java).findAll().deleteAllFromRealm()
        }
        realm.close()
    }

//    fun getTokenfromRealm() {
//        val realm = Realm.getDefaultInstance()
//        val result = realm.where(AccessToken::class.java).findAll()
//        if (result.isNotEmpty()) {
//            val items = AccessToken().apply {
//                Collections.addAll(realm.copyFromRealm(result))
//            }
//            view.onLogin(ResultState.Success(items))
//        } else {
//            view.onLogin(ResultState.Error("No data in Realm"))
//        }
//        realm.close()
//    }
}