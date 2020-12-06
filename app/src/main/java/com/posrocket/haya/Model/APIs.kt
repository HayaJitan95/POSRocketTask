package com.posrocket.haya.Model

import io.reactivex.Single
import retrofit2.http.GET

interface APIs {
    @GET("v1/317eac85")
    fun getCustomers(): Single<CustomerResponse>
}