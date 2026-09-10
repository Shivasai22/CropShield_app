package com.cropshield.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PredictionHistoryScreen(
    predictions: List<Prediction>,
    onBack: () -> Unit,
    onClear: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Prediction History"
        )

        Button(
            onClick = onBack,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Back")
        }

        Button(
            onClick = onClear,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Clear History")
        }

        if (predictions.isEmpty()) {

            Text(
                text = "No predictions saved yet.",
                modifier = Modifier.padding(top = 20.dp)
            )

        } else {

            LazyColumn(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                items(predictions) { prediction ->

                    Card {

                        Column(
                            modifier =
                                Modifier.padding(16.dp)
                        ) {

                            Text(
                                text =
                                    "Disease: ${prediction.disease}"
                            )

                            Text(
                                text =
                                    "Confidence: %.2f%%"
                                        .format(
                                            prediction.confidence
                                        )
                            )

                            Text(
                                text =
                                    "Date: ${prediction.dateTime}"
                            )

                            Text(
                                text =
                                    "Mode: ${prediction.mode}"
                            )
                        }
                    }
                }
            }
        }
    }
}