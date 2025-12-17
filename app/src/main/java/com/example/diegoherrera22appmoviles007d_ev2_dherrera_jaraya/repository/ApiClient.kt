package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.api.AuthApi
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.api.UserApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://3.129.19.7:8080/"

    private lateinit var tokenDataStore: TokenDataStore

    fun init(dataStore: TokenDataStore) {
        tokenDataStore = dataStore
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenDataStore))
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // 👈 ESTO ES LO QUE TE FALTABA
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val regionApi: RegionComunaRepository by lazy {
        retrofit.create(RegionComunaRepository::class.java)
    }
}

