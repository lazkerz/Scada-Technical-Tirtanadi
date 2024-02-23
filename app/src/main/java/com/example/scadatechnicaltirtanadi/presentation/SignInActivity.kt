package com.example.scadatechnicaltirtanadi.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.apps_magang.utils.RealmManager
import com.example.apps_magang.utils.ResultState
import com.example.mateup.view.user_view
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

        val username =

        presenter.getUser(username: String, password: String)
    }

    override fun onLogin(result: ResultState<AccessToken>) {
        when (result) {
            is ResultState.Success -> {
                // Handle data berhasil diterima
                val Token = result.data
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