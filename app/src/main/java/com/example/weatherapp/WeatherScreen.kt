package com.example.weatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WeatherScreen() {

    val viewModeObj: WeatherViewModel = viewModel()
    val weatherstate by viewModeObj.state_for_comp
    val user_input by viewModeObj.userInput
    val inttemp = weatherstate.temp.toInt()

//    Column(modifier = Modifier.padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ){
//
//        OutlinedTextField(value = user_input, onValueChange = { viewModeObj.onUserInputChange(it) })
//        Spacer(modifier = Modifier.padding(8.dp))
//
//        Button(onClick = { viewModeObj.getData() }) {
//            Text(text = "Get Weather")
//        }
//
//        Text(text = "Current Temperature: ${weatherstate.temp}", fontSize = 16.sp)
//        Text(text = "City: ${weatherstate.City}", fontSize = 16.sp)
//        Text(text = "State: ${weatherstate.State}", fontSize = 16.sp)
//        Text(text = "Country: ${weatherstate.Country}", fontSize = 16.sp)
//
//    }

    Column(modifier = Modifier
        .fillMaxSize()
        .paint(painterResource(id = R.drawable.uifinal)),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Box(modifier = Modifier.weight(0.62f),
            contentAlignment = Alignment.CenterStart)
        {
            Text(text = "${inttemp}°", fontSize = 72.sp,
                modifier = Modifier.padding(top = 96.dp),


            )
        }
        Box(modifier = Modifier
            .weight(0.38f)
            .fillMaxWidth()
            .background(Color.Green)){
            Column {
                Text(text = "Santa Cruz", fontSize = 32.sp, modifier = Modifier.padding(top = 16.dp).padding(horizontal = 36.dp))
//                Text(text = "_________", letterSpacing = 1, fontSize = 32.sp,modifier = Modifier.padding(horizontal = 36.dp))
            }


        }


    }


            
    }
