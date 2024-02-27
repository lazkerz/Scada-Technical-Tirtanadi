package com.example.scadatechnicaltirtanadi.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Toast
import com.example.apps_magang.utils.ResultState
import com.example.mateup.view.user_view
import com.example.scadatechnicaltirtanadi.R
import com.example.scadatechnicaltirtanadi.data.AccessToken
import com.example.scadatechnicaltirtanadi.presenter.UserData
import com.example.scadatechnicaltirtanadi.remote.ApiConfig
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class OverviewActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        val apiService = ApiConfig.getToken(this)
        if (apiService != null) {
            presenter = UserData(
                apiService,
                this,
            )
        } else {
            Log.e("SignIn", "Failed to initialize ApiService")
            Toast.makeText(this, "Failed to initialize ApiService", Toast.LENGTH_SHORT)
                .show()
        }

        val webView: WebView = findViewById(R.id.overview)
        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        val accessToken = presenter.getAccessToken()
        val branchSlug = intent.getStringExtra("branch_slug") ?: ""

        val customWebViewClient = CustomWebViewClient("${accessToken}")
        webView.webViewClient = customWebViewClient // Menggunakan CustomWebViewClient

        webView.loadUrl("https://scada-technical.tirtanadi.ibmsindo.net/${branchSlug}/overview")

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.setSupportZoom(true) // Aktifkan kontrol zoom
        webSettings.builtInZoomControls = true // Aktifkan kontrol zoom bawaan WebView
        webSettings.displayZoomControls = false
    }

    class CustomWebViewClient(private val accessToken: String) : WebViewClient() {

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            // Periksa jika request tidak null dan URL request tidak null
            if (request != null && request.url != null) {
                // Ambil URL request
                val url = request.url.toString()

                // Periksa jika URL adalah URL yang ingin Anda tambahkan header access token
                if (url.contains("https://scada-technical.tirtanadi.ibmsindo.net")) {
                    val conn = URL(url).openConnection() as HttpURLConnection

                    // Tambahkan header access token ke permintaan
                    if (!accessToken.isNullOrEmpty()) {
                        conn.setRequestProperty("Authorization", "Bearer $accessToken")
                    }

                    // Baca respons dari server
                    try {
                        conn.connect()
                        val contentType = conn.contentType
                        val contentEncoding = conn.contentEncoding

                        // Buat dan kembalikan WebResourceResponse dengan data yang dibaca
                        return conn.inputStream.use { inputStream ->
                            WebResourceResponse(
                                contentType,
                                contentEncoding,
                                inputStream
                            )
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        conn.disconnect()
                    }
                }
            }
            // Biarkan WebView menangani permintaan jika tidak memerlukan manipulasi header access token
            return super.shouldInterceptRequest(view, request)
        }
    }


    override fun onLogin(result: ResultState<AccessToken>) {
        when (result) {
            is ResultState.Success -> {
                val accessToken = result.data
            }
            is ResultState.Error -> {
                // Handle jika terjadi error
                val errorMessage = result.error
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                // Handle loading state
                Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
