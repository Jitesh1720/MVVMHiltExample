package com.example.demokotlinapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
@Parcelize
data class User(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("address") val address: Address,
    @SerializedName("phone") val phone: String,
    @SerializedName("website") val website: String,
    @SerializedName("company") val company: Company
) : Parcelable {
    val avatarUrl: String
        get() = "https://robohash.org/${username}.png?size=200x200&set=set4"
}

@Parcelize
data class Address(
    @SerializedName("street") val street: String,
    @SerializedName("suite") val suite: String,
    @SerializedName("city") val city: String,
    @SerializedName("zipcode") val zipcode: String,
    @SerializedName("geo") val geo: Geo
) : Parcelable

@Parcelize
data class Geo(
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String
) : Parcelable

@Parcelize
data class Company(
    @SerializedName("name") val name: String,
    @SerializedName("catchPhrase") val catchPhrase: String,
    @SerializedName("bs") val bs: String
) : Parcelable
