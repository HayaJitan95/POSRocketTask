package com.posrocket.haya.Model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Service {
    private val BASE_URL = "https://api.mocki.io/"
    private val api: APIs

    init {
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(APIs::class.java)
    }

    fun getCustomers(): Single<CustomerResponse> {
        return api.getCustomers()
    }
}