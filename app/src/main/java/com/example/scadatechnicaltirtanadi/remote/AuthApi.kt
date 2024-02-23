package com.example.scadatechnicaltirtanadi.remote

import com.example.scadatechnicaltirtanadi.data.AccessToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
        @Headers("Accept: application/json")
        @FormUrlEncoded
        @POST("oauth")
        fun signIn(
                @Field("Username") username: String,
                @Field("Password") password: String
        ): Call<AccessToken>
}