package com.cropshield.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DiseaseAdviceScreen(
    advice: DiseaseAdvice,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Disease Advice"
        )

        Text(
            text = advice.disease
        )

        Card {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Description"
                )

                Text(
                    text = advice.description,
                    modifier =
                        Modifier.padding(top = 6.dp)
                )
            }
        }

        Card {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Symptoms"
                )

                Text(
                    text = advice.symptoms,
                    modifier =
                        Modifier.padding(top = 6.dp)
                )
            }
        }

        Card {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Recommended Action"
                )

                Text(
                    text = advice.recommendation,
                    modifier =
                        Modifier.padding(top = 6.dp)
                )
            }
        }

        Card {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Prevention"
                )

                Text(
                    text = advice.prevention,
                    modifier =
                        Modifier.padding(top = 6.dp)
                )
            }
        }

        Button(
            onClick = onBack
        ) {

            Text(
                text = "Back to Detection"
            )
        }
    }
}