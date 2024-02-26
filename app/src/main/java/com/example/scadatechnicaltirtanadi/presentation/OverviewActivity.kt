package com.example.scadatechnicaltirtanadi.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://scada-technical.tirtanadi.ibmsindo.net/${branchSlug}/overview")

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.setSupportZoom(true) // Aktifkan kontrol zoom
        webSettings.builtInZoomControls = true // Aktifkan kontrol zoom bawaan WebView
        webSettings.displayZoomControls = false
   



//        val url = "https://scada-technical.tirtanadi.ibmsindo.net/${branchSlug}/overview"
//
//        val webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
//                // Tambahkan header permintaan HTTP
//                val connection = URL(url).openConnection() as HttpURLConnection
////                connection.setRequestProperty("Authorization: ", "Bearer $accessToken")
//                try {
//                    connection.connect()
//                    view?.loadUrl(url)
//                } catch (e: IOException) {
//                    // Tangani kesalahan
//                    e.printStackTrace()
//                } finally {
//                    connection.disconnect()
//                }
//                return true
//            }
//        }
//
//        webView.settings.apply {
//            javaScriptEnabled = true
//            setSupportZoom(false)
//            builtInZoomControls = false
//            displayZoomControls = false
//            loadWithOverviewMode = true
//            cacheMode = WebSettings.LOAD_DEFAULT
//        }
//
//        webView.webViewClient = webViewClient
//        webView.loadUrl(url)
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
