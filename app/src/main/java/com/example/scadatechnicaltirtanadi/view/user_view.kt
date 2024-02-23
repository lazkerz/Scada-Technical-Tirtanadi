package com.example.mateup.view

import com.example.apps_magang.utils.ResultState
import com.example.scadatechnicaltirtanadi.data.AccessToken

interface user_view {
    fun onLogin(result: ResultState<AccessToken>)
}