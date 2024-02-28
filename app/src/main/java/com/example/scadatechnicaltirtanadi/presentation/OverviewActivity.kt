package com.example.scadatechnicaltirtanadi.presentation

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.apps_magang.utils.ResultState
import com.example.mateup.view.user_view
import com.example.scadatechnicaltirtanadi.R
import com.example.scadatechnicaltirtanadi.data.AccessToken
import com.example.scadatechnicaltirtanadi.presenter.UserData
import com.example.scadatechnicaltirtanadi.remote.ApiConfig

class OverviewActivity : AppCompatActivity(), user_view {

    private lateinit var presenter: UserData
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        val apiService = ApiConfig.getToken(this)
        if (apiService != null) {
            presenter = UserData(
                apiService,
                this,
                this
            )
        } else {
            Log.e("SignIn", "Failed to initialize ApiService")
            Toast.makeText(this, "Failed to initialize ApiService", Toast.LENGTH_SHORT)
                .show()
        }

        webView = findViewById(R.id.overview)
        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val accessToken = presenter.getAccessToken()
        val branchSlug = intent.getStringExtra("branch_slug") ?: ""

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $accessToken"

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Set visibilitas WebView ke VISIBLE setelah konten selesai dimuat
                webView.visibility = View.VISIBLE
                // Sembunyikan tampilan loading setelah halaman selesai dimuat
                setLoading(false)
                // Periksa apakah halaman berhasil dimuat
                if (!view?.title.isNullOrEmpty() && view?.title?.toLowerCase() == "untitled") {
                    // Halaman tidak berhasil dimuat, tampilkan dialog untuk memuat ulang
                    showReloadDialog()
                }
            }
        }

        // Set visibilitas WebView ke GONE pada awalnya
        webView.visibility = View.GONE

        // Load URL ke WebView
        webView.loadUrl("https://scada-technical.tirtanadi.ibmsindo.net/${branchSlug}/overview", headers)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.setSupportZoom(true) // Aktifkan kontrol zoom
        webSettings.builtInZoomControls = true // Aktifkan kontrol zoom bawaan WebView
        webSettings.displayZoomControls = false
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
    }

    private fun showReloadDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konten tidak dapat dimuat")
        builder.setMessage("Apakah Anda ingin memuat ulang konten?")
        builder.setPositiveButton("Ya") { dialog, which ->
            // Panggil metode untuk memuat ulang konten WebView
            webView.reload()
            dialog.dismiss() // Tutup dialog setelah pengguna menekan tombol Ya
        }
        builder.setNegativeButton("Tidak") { dialog, which ->
            dialog.dismiss() // Tutup dialog jika pengguna memilih untuk tidak memuat ulang konten
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setLoading(isLoading: Boolean) {
        val viewLoading = findViewById<RelativeLayout>(R.id.view_loading)

        if (isLoading) {
            // Tampilkan tampilan loading
            viewLoading.visibility = View.VISIBLE
            webView.visibility = View.GONE
        } else {
            // Sembunyikan tampilan loading
            viewLoading.visibility = View.GONE
            webView.visibility = View.VISIBLE
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
