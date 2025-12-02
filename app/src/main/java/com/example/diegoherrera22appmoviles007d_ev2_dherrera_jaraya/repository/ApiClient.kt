package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val regionApi: RegionComunaRepository =
        retrofit.create(RegionComunaRepository::class.java)
}
