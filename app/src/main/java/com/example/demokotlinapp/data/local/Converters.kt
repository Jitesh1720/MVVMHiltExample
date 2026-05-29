package com.example.demokotlinapp.data.local

import androidx.room.TypeConverter
import com.example.demokotlinapp.model.Address
import com.example.demokotlinapp.model.Company
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromAddress(address: Address?): String? {
        return gson.toJson(address)
    }

    @TypeConverter
    fun toAddress(addressString: String?): Address? {
        if (addressString == null) return null
        val type = object : TypeToken<Address>() {}.type
        return gson.fromJson(addressString, type)
    }

    @TypeConverter
    fun fromCompany(company: Company?): String? {
        return gson.toJson(company)
    }

    @TypeConverter
    fun toCompany(companyString: String?): Company? {
        if (companyString == null) return null
        val type = object : TypeToken<Company>() {}.type
        return gson.fromJson(companyString, type)
    }
}
