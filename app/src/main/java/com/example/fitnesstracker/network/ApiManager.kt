package com.example.fitnesstracker.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

object ApiManager {

    fun sendHeartRateDataToBackend(trackingNumber: Int, startTime: Long, endTime: Long, heartRate: Int) {
        val startTimeFormatted = formatTime(startTime) // Format start time as string
        val endTimeFormatted = formatTime(endTime) // Format end time as string

        // Call the API method from Retrofit
        RetrofitInstance.api.postHeartRateDataV2(
            trackingNumber = trackingNumber,
            startTime = startTimeFormatted,
            endTime = endTimeFormatted,
            heartRate = heartRate
        ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.d("API", "Data sent successfully: ${response.body()}")
                } else {
                    Log.e("API", "Error sending data: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API", "Request failed: ${t.message}")
            }
        })
    }

    // Helper function to format time
    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
