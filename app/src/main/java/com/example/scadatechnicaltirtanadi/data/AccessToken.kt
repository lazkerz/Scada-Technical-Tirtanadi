package com.example.scadatechnicaltirtanadi.data

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import java.io.Serializable

@RealmClass
open class AccessToken: RealmObject(), Serializable {

	@SerializedName("access_token")
	var accessToken: String? = null

	@SerializedName("refresh_token")
	var refreshToken: String? = null

	@SerializedName("token_type")
	var tokenType: String? = null

	@SerializedName("expires_in")
	var expiresIn: Int = 0
}
