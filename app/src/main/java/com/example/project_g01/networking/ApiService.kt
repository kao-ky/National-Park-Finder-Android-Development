package com.example.project_g01.networking

import com.example.project_g01.models.AllPark
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/v1/parks/")
    suspend fun getAllPark(
        @Query("stateCode") stateCode: String,
        @Query("api_key") apiKey: String
    ): Response<AllPark>
}