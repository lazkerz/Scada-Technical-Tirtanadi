package com.example.scadatechnicaltirtanadi.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
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
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        val apiService = ApiConfig.getToken(this)
        if (apiService != null) {
            presenter = UserData(apiService, this)
        } else {
            Log.e("SignIn", "Failed to initialize ApiService")
            Toast.makeText(this, "Failed to initialize ApiService", Toast.LENGTH_SHORT).show()
        }

        webView = findViewById(R.id.overview)
        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        val accessToken = presenter.getRefreshToken()
        Log.d("Token:", "${accessToken}")
        val branchSlug = intent.getStringExtra("branch_slug") ?: ""

        webView.webViewClient = CustomWebViewClient()

        // Tambahkan AccessToken ke header WebView
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $accessToken"
        webView.loadUrl("https://scada-technical.tirtanadi.ibmsindo.net/${branchSlug}/overview", headers)
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

    private inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            // Handle URL loading here if needed
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
}
