package com.cropshield.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CropShieldHomeScreen(
    username: String,
    onScanPlant: () -> Unit,
    onHistory: () -> Unit,
    onLogout: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "CropShield"
        )

        Text(
            text = "Welcome, $username",
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(
            onClick = onScanPlant,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text("Scan Plant")
        }

        Button(
            onClick = onHistory,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Prediction History")
        }

        Button(
            onClick = onLogout,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Logout")
        }
    }
}