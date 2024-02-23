package com.example.scadatechnicaltirtanadi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.apps_magang.utils.LoginManager
import com.example.apps_magang.utils.ResultState
import com.example.mateup.view.user_view
import com.example.scadatechnicaltirtanadi.data.AccessToken
import com.example.scadatechnicaltirtanadi.presentation.SignInActivity
import com.example.scadatechnicaltirtanadi.presenter.UserData
import com.example.scadatechnicaltirtanadi.remote.ApiConfig

class MainActivity : AppCompatActivity(), user_view{
        private lateinit var presenter: UserData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var logout = findViewById<Button>(R.id.btn_logout)

        val apiService = ApiConfig.getToken(this)

        presenter = UserData(
            apiService,
            this,
        )

        logout.setOnClickListener {
            presenter.deleteToken()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish() // Optional: Menutup aktivitas saat ini setelah logout
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