package com.example.scadatechnicaltirtanadi.remote

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getToken(context: Context): AuthApi {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(createChuckerInterceptor(context))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://scada-technical.tirtanadi.ibmsindo.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        return retrofit.create(AuthApi::class.java)
    }

    private fun createChuckerInterceptor(context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    }
}
