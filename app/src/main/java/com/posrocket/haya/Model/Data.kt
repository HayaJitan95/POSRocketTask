package com.posrocket.haya.Model

import com.google.gson.annotations.SerializedName


data class CustomerResponse(
    @SerializedName("data")
    val customerlist: List<Customer>?
)

data class Customer(
    @SerializedName("first_name")
    val name: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("balance")
    val balance: String?,
    @SerializedName("gender")    //   UNSPECIFIED , MALE , FEMALE
    val gender: String?,
    @SerializedName("phone_numbers")
    val mobile: List<PhoneNumbers>?,
    @SerializedName("tags")
    val tags: List<Tags>?
)

data class PhoneNumbers(
    @SerializedName("id")
    val id: String?,
    @SerializedName("is_primary")
    val isPrimary: Boolean?,
    @SerializedName("is_verified")
    val isVerified: Boolean?,
    @SerializedName("number")
    val number: String?
)

data class Tags(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)