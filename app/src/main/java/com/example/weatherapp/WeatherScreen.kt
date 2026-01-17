package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@Composable
fun WeatherScreen() {


    val viewModeObj: WeatherViewModel = viewModel()
    val weatherstate by viewModeObj.state_for_comp
    val user_input by viewModeObj.userInput


    var currentTime by remember { mutableStateOf(Date()) }

    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }


    fun fetchLocation() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { location ->
                if (location != null) {
                    viewModeObj.getWeatherByCoordinates(location.latitude, location.longitude)
                } else {
                    fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                        lastLocation?.let {
                            viewModeObj.getWeatherByCoordinates(it.latitude, it.longitude)
                        }
                    }
                }
            }
        } else {
            Log.e("fetchLocation", "Location permission not granted")
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fetchLocation()
        } else {
            Log.e("WeatherScreen", "❌ Location permission denied")
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            fetchLocation()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000L)
        }
    }

    val formattedDate = remember(currentTime, weatherstate.TimeZone) {
        try {
            val df = SimpleDateFormat("MMMM d", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone(weatherstate.TimeZone)
            df.format(currentTime)
        } catch (e: Exception) {
            ""
        }
    }

    val formattedTime = remember(currentTime, weatherstate.TimeZone) {
        try {
            val tf = SimpleDateFormat("h:mma", Locale.ENGLISH)
            tf.timeZone = TimeZone.getTimeZone(weatherstate.TimeZone)
            tf.format(currentTime).lowercase()
        } catch (e: Exception) {
            ""
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (weatherstate.City.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
//            .paint(painterResource(id = R.drawable.uifinal))
//                .background(Brush.verticalGradient(listOf(Color(0xFF355C7D), Color(0xFF80b3c9))))
                    .background(Color(0xFF0172B0))
                    .background(Color.Black.copy(alpha = 0.1f))

            )
            {
                SearchBar(
                    query = user_input,
                    onQueryChanged = {
                        viewModeObj.onUserInputChange(it)
                    }
                )

                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxWidth() // IMPORTANT: Ensures the Box uses the full horizontal space
                )

                {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentSize(align = Alignment.TopStart)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF75D6FF).copy(alpha = 0.2f))
                            .padding(8.dp)

                    ) {

                        Column {

                            Text(
                                text = weatherstate.City,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            )

                            Divider(
                                color = Color.White,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .padding(end = 36.dp)
                                    .width(180.dp)
                                    .graphicsLayer(alpha = 0.4f)
                            )

                            Text(
                                text = formattedDate, fontSize = 20.sp,
                                modifier = Modifier.graphicsLayer(alpha = 0.95f),
                                color = Color.White
                            )

                            Text(
                                text = formattedTime, fontSize = 16.sp,
                                modifier = Modifier.graphicsLayer(alpha = 0.95f),
                                color = Color.White
                            )

                            Row {

                                Text(
                                    text = "RAIN PROBABILITY\n${weatherstate.ChanceOfRain}%",
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .padding(end = 8.dp)
                                        .graphicsLayer(alpha = 0.9f),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )

                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(32.dp)
                                        .background(Color.White)
                                        .align(Alignment.CenterVertically)
                                        .padding(top = 32.dp)
                                        .graphicsLayer(alpha = 0.4f)
                                )

                                Text(
                                    text = "HUMIDITY\n${weatherstate.Humidity}%",
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .padding(start = 8.dp)
                                        .graphicsLayer(alpha = 0.9f),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }

                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(top = 24.dp)
                            .wrapContentSize()
                            .background(
                                Color(0xFF75D6FF).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .align(Alignment.CenterStart)
                            .padding(end = 16.dp)
                    ) {

                        Text(
                            text = "Feels Like: ${weatherstate.FeelsLike}°C",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Text(
                        text = "${weatherstate.temp.toInt()}°",
                        fontSize = 70.sp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 48.dp),
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxWidth()
                ) {
                    HourlyCards(weatherstate.HourlyForecast)
                }
            }
        } else if (!weatherstate.loading) {
            // Case where app isn't loading but has no data (e.g., error or start)

            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF75D6FF)))
        }
        if (weatherstate.loading) {
            LoadingOverlay()
        }
    }

}

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModeObj: WeatherViewModel = viewModel()
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Enter a city name") },
        textStyle = TextStyle(Color.Black),
        trailingIcon = {
            IconButton(onClick = {
                viewModeObj.getData()
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null, tint = Color.Black
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.LightGray
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(
            onGo = {
                viewModeObj.getData()
                keyboardController?.hide()
            }
        )
    )
}


//when{
//    viewState.loading->{
//        CircularProgressIndicator(modifier.align(Alignment.Center))
//    }
//    viewState.error != null ->{
//        Text(text = "ERROR OCCURED!")
//    }
//    else->{
//        CategoryScreen(viewState.list)
//    }
//}