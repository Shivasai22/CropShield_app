package com.cropshield.app

data class Prediction(
    val username: String,
    val disease: String,
    val confidence: Float,
    val dateTime: String,
    val mode: String
)