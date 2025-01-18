package com.example.fitnesstracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val TAG = "GoogleFit"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fitnessOptions = getFitnessOptions()
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this,
                1,
                account,
                fitnessOptions
            )
        }

        setContent {
            // Pass a callback to fetch heart rate data and update UI
            FitnessTrackerApp { fetchHeartRateData() }
        }
    }

    private fun fetchHeartRateData(): List<TrackingData> {
        val account = GoogleSignIn.getAccountForExtension(this, getFitnessOptions())
        val endTime = System.currentTimeMillis()
        val startTime = endTime - TimeUnit.DAYS.toMillis(7)

        val request = DataReadRequest.Builder()
            .read(DataType.TYPE_HEART_RATE_BPM)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()

        val trackingDataList = mutableListOf<TrackingData>()

        // Use Google Fit API's Task and listeners to handle async response
        val task: Task<DataReadResponse> = Fitness.getHistoryClient(this, account).readData(request)
        task.addOnSuccessListener { response ->
            // Process the response on success
            for (dataSet in response.dataSets) {
                for (dataPoint in dataSet.dataPoints) {
                    val heartRate = extractHeartRate(dataPoint)
                    if (heartRate != null) {
                        val trackingData = TrackingData(
                            trackingNumber = trackingDataList.size + 1,
                            startTime = formatTime(dataPoint.getStartTime(TimeUnit.MILLISECONDS)),
                            endTime = formatTime(dataPoint.getEndTime(TimeUnit.MILLISECONDS)),
                            heartRate = heartRate.toString()
                        )
                        trackingDataList.add(trackingData)
                    }
                }
            }
        }
        task.addOnFailureListener { exception ->
            Log.e(TAG, "Failed to fetch heart rate data.", exception)
        }

        return trackingDataList
    }

    private fun extractHeartRate(dataPoint: DataPoint): Float? {
        return dataPoint.getValue(DataType.TYPE_HEART_RATE_BPM.fields[0])?.asFloat()
    }

    private fun getFitnessOptions(): FitnessOptions {
        return FitnessOptions.builder()
            .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
            .build()
    }
}

@Composable
fun FitnessTrackerApp(fetchHeartRateData: () -> List<TrackingData>) {
    var trackingDataList by remember { mutableStateOf(listOf<TrackingData>()) }

    LaunchedEffect(true) {
        // Fetch heart rate data asynchronously and update the list
        val fetchedData = fetchHeartRateData()
        if (fetchedData.isNotEmpty()) {
            trackingDataList = fetchedData
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.align(Alignment.TopStart),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TableRow(
                    column1 = "Track No",
                    column2 = "Start Time",
                    column3 = "End Time",
                    column4 = "Heart Rate",
                    isHeader = true
                )
            }
            items(trackingDataList) { data ->
                TableRow(
                    column1 = data.trackingNumber.toString(),
                    column2 = data.startTime,
                    column3 = data.endTime,
                    column4 = data.heartRate,
                    isHeader = false
                )
            }
        }

        FloatingActionButton(
            onClick = {
                // Add new data when button is clicked
                trackingDataList = fetchHeartRateData()
            },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(64.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Text("+", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TableRow(column1: String, column2: String, column3: String, column4: String, isHeader: Boolean) {
    val backgroundColor = if (isHeader) MaterialTheme.colorScheme.primary else Color.Transparent
    val textColor = if (isHeader) Color.White else MaterialTheme.colorScheme.onBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = column1, color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = column2, color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = column3, color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = column4, color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

data class TrackingData(
    val trackingNumber: Int,
    val startTime: String,
    val endTime: String,
    val heartRate: String
)
