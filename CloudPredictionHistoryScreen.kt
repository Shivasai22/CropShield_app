package com.cropshield.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CloudPredictionHistoryScreen(
    localPredictions: List<Prediction>,
    firebasePredictionStorage: FirebasePredictionStorage,
    onBack: () -> Unit
) {

    var cloudPredictions by remember {
        mutableStateOf<List<Prediction>>(
            emptyList()
        )
    }

    var message by remember {
        mutableStateOf(
            "Loading cloud history..."
        )
    }

    /*
     * Load Firebase predictions
     */
    LaunchedEffect(Unit) {

        firebasePredictionStorage
            .getPredictions { predictions, error ->

                if (error != null) {

                    message = error

                } else {

                    cloudPredictions =
                        predictions

                    message =
                        if (predictions.isEmpty()) {
                            "No cloud predictions"
                        } else {
                            "${predictions.size} cloud prediction(s)"
                        }
                }
            }
    }

    /*
     * Combine local + cloud history.
     *
     * Remove exact duplicates.
     */
    val allPredictions =
        remember(
            localPredictions,
            cloudPredictions
        ) {

            (
                    localPredictions +
                            cloudPredictions
                    )
                .distinctBy {

                    "${it.disease}_" +
                            "${it.dateTime}_" +
                            "${it.confidence}"
                }
                .sortedByDescending {

                    it.dateTime
                }
        }

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp),

        verticalArrangement =
            Arrangement.Top
    ) {

        Text(
            text = "Prediction History"
        )

        Spacer(
            modifier =
                Modifier.padding(8.dp)
        )

        Text(
            text = message
        )

        Spacer(
            modifier =
                Modifier.padding(8.dp)
        )

        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
        ) {

            items(
                allPredictions
            ) { prediction ->

                Card(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 6.dp
                            )
                ) {

                    Column(

                        modifier =
                            Modifier.padding(16.dp)
                    ) {

                        Text(
                            text =
                                "Disease: " +
                                        prediction.disease
                        )

                        Text(
                            text =
                                "Confidence: " +
                                        "%.2f%%"
                                            .format(
                                                prediction.confidence
                                            )
                        )

                        Text(
                            text =
                                "Date: " +
                                        prediction.dateTime
                        )

                        Text(
                            text =
                                "Mode: " +
                                        prediction.mode
                        )
                    }
                }
            }
        }

        Button(

            onClick = onBack,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
        ) {

            Text(
                text = "Back to Home"
            )
        }
    }
}