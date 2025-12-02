package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Region
import retrofit2.http.GET

interface RegionComunaRepository {

    @GET("regions")
    suspend fun getRegions(): List<Region>
}