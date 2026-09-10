package com.cropshield.app

data class DiseaseAdvice(
    val disease: String,
    val description: String,
    val symptoms: String,
    val recommendation: String,
    val prevention: String
)