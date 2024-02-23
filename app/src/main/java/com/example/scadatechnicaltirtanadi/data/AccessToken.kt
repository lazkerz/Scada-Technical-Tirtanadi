package com.example.scadatechnicaltirtanadi.data

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import java.io.Serializable


@RealmClass
open class AccessToken: RealmObject(), Serializable{

	@field:SerializedName("access_token")
	val accessToken: String? = null

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null

	@field:SerializedName("token_type")
	val tokenType: String? = null

	@field:SerializedName("expires_in")
	val expiresIn: Int = 0
}
