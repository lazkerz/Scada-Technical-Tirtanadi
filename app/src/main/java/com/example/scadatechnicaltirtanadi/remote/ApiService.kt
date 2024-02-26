package com.example.scadatechnicaltirtanadi.remote

import com.example.scadatechnicaltirtanadi.data.AccessToken
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
        @Headers("Accept: application/json")
        @POST("oauth")
        fun signIn(@Body signInRequest: SignInRequest): Call<AccessToken>
}

data class SignInRequest(
        @SerializedName("password") val password: String,
        @SerializedName("username") val username: String,
        @SerializedName("grant_type") val grantType: String,
        @SerializedName("client_secret") val clientSecret: String,
        @SerializedName("client_id") val clientId: String
)

interface Overview {
        @Headers("Accept: application/hal+json")
        @GET("api/v1//ipam-hourly")
        fun getOverview(
                @Query("branch_slug") branch_slug: String,
                @Query("hour") hour: String,
                @Query("date") date: String,
        ): Call<AccessToken>
}