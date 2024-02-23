package com.example.scadatechnicaltirtanadi.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.apps_magang.utils.LoginManager
import com.example.apps_magang.utils.RealmManager
import com.example.apps_magang.utils.ResultState
import com.example.mateup.view.user_view
import com.example.scadatechnicaltirtanadi.MainActivity
import com.example.scadatechnicaltirtanadi.R
import com.example.scadatechnicaltirtanadi.data.AccessToken
import com.example.scadatechnicaltirtanadi.presenter.UserData
import com.example.scadatechnicaltirtanadi.remote.ApiConfig
import io.realm.Realm

class SignInActivity : AppCompatActivity(), user_view {
    private lateinit var presenter: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        Realm.init(this)
        RealmManager.initRealm()

        val apiService = ApiConfig.getToken(this)

        presenter = UserData(
            apiService,
            this,
        )

        val username = findViewById<TextView>(R.id.etUsername)
        val password = findViewById<TextView>(R.id.etPassword)
        val buttonLogin = findViewById<Button>(R.id.btnSignIn)

        buttonLogin.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()

            presenter.getUser(username, password)
        }
    }


    override fun onLogin(result: ResultState<AccessToken>) {
        when (result) {
            is ResultState.Success -> {
                val accessToken = result.data
                // Lanjutkan ke MainActivity atau tindakan yang sesuai
                startActivity(Intent(this, MainActivity::class.java))
                // Simpan status login
                LoginManager.saveLogin(this, true)
                finish()
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