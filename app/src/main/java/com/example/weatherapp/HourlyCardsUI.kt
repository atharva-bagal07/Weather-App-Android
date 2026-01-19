package com.example.weatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun HourlyCards(hourlyForecast: List<HourlyData>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(hourlyForecast) { hour ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        Color(0xFF75D6FF).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(24.dp)
            ) {
                // Time
                val hourStr = hour.time.substringAfter(" ").substring(0, 2) // "18" from "18:00"
                val minuteStr = hour.time.substringAfter(":")              // "00"

                val hourInt = hourStr.toInt()
                val amPm = if (hourInt >= 12) "PM" else "AM"
                val hour12 = when {
                    hourInt == 0 -> 12
                    hourInt > 12 -> hourInt - 12
                    else -> hourInt
                }

                Text(
                    text = "$hour12:$minuteStr $amPm",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )


                Spacer(modifier = Modifier.height(8.dp))

                AsyncImage(
                    model = "https:" + hour.condition.icon,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )



                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${hour.temp_c}°C", fontWeight = FontWeight.Bold, color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
}