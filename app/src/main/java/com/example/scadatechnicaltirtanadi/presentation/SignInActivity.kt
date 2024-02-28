package com.example.scadatechnicaltirtanadi.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.apps_magang.utils.LoginManager
import com.example.apps_magang.utils.RealmManager
import com.example.apps_magang.utils.ResultState
import com.example.mateup.view.user_view
import com.example.scadatechnicaltirtanadi.R
import com.example.scadatechnicaltirtanadi.data.AccessToken
import com.example.scadatechnicaltirtanadi.presenter.UserData
import com.example.scadatechnicaltirtanadi.remote.ApiConfig
import com.example.scadatechnicaltirtanadi.remote.AuthApi
import com.google.android.material.textfield.TextInputLayout
import io.realm.Realm

class SignInActivity : AppCompatActivity(), user_view {
    private lateinit var presenter: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        Realm.init(this)
        RealmManager.initRealm()

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

        val username = findViewById<TextView>(R.id.etUsername)
        val password = findViewById<TextView>(R.id.etPassword)
        val buttonLogin = findViewById<Button>(R.id.btnSignIn)

        val authPasswordTextLayout = findViewById<TextInputLayout>(R.id.etPasswordLayout)

        authPasswordTextLayout.setEndIconOnClickListener {
            // Mengubah visibilitas teks password sesuai status saat ini
            val editText = authPasswordTextLayout.editText
            editText?.let {
                if (editText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    // Jika teks password disembunyikan, tampilkan
                    editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    // Jika teks password ditampilkan, sembunyikan
                    editText.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                // Memastikan teks kembali ke posisi terakhir
                editText.setSelection(editText.text.length)
            }
        }

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