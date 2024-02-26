package com.example.scadatechnicaltirtanadi.remote

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig private constructor() {
    companion object {
//        inline fun getApiService(context: Context, apiType: String): Any? {
//            val chuckerInterceptor = ChuckerInterceptor.Builder(context)
//                .collector(ChuckerCollector(context))
//                .maxContentLength(250000L)
//                .redactHeaders(emptySet())
//                .alwaysReadResponseBody(false)
//                .build()
//
//            val BASE_URL = "https://scada-technical.tirtanadi.ibmsindo.net/"
//
//            val retrofit = Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//
//            val client = OkHttpClient.Builder()
//                .addInterceptor(chuckerInterceptor)
//                .build()
//
//            return when (apiType) {
//                "token" -> {
//                    retrofit.newBuilder().client(client).build().create(AuthApi::class.java)
//                }
//
//                "overview" -> {
//                    retrofit.newBuilder().client(client).build().create(Overview::class.java)
//                }
//
//                else -> throw IllegalArgumentException("Unknown API type: $apiType")
//            }
//        }
//    }
//
//
//}
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
}