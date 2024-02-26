package com.example.scadatechnicaltirtanadi.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.apps_magang.utils.RealmManager
import com.example.apps_magang.utils.ResultState
import com.example.mateup.view.user_view
import com.example.scadatechnicaltirtanadi.R
import com.example.scadatechnicaltirtanadi.data.AccessToken
import com.example.scadatechnicaltirtanadi.presenter.UserData
import com.example.scadatechnicaltirtanadi.remote.ApiConfig
import com.example.scadatechnicaltirtanadi.remote.AuthApi
import io.realm.Realm
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), user_view{
        private lateinit var presenter: UserData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)
        RealmManager.initRealm()

        var logout = findViewById<ImageView>(R.id.btn_logout)

        var Delitua= findViewById<LinearLayout>(R.id.delitua)
        var Sunggal= findViewById<LinearLayout>(R.id.sunggal)
        var Martubung= findViewById<LinearLayout>(R.id.martubung)
        var Limaumanis= findViewById<LinearLayout>(R.id.limauManis)
        var Hamparanperak= findViewById<LinearLayout>(R.id.hamparanPerak)


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

        Delitua.setOnClickListener {
            navigateToWebView("delitua")
        }

        Sunggal.setOnClickListener {
            navigateToWebView("sunggal")
        }

        Martubung.setOnClickListener {
            navigateToWebView("martubung")
        }

        Limaumanis.setOnClickListener {
            navigateToWebView("limauManis")
        }

        logout.setOnClickListener {
            presenter.deleteToken()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun navigateToWebView(branchSlug: String) {
            val intent = Intent(this, OverviewActivity::class.java)
            intent.putExtra("branch_slug", branchSlug)
            startActivity(intent)
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