package com.example.fitnesstracker.network

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("/postHeartRateDataV2")
    fun postHeartRateDataV2(
        @Query("trackingNo") trackingNumber: Int,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String,
        @Query("heartRate") heartRate: Int
    ): Call<String>
}
