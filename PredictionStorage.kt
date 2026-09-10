package com.cropshield.app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class PredictionStorage(context: Context) {

    private val prefs =
        context.getSharedPreferences(
            "CropShieldPredictions",
            Context.MODE_PRIVATE
        )

    companion object {
        private const val PREDICTIONS_KEY = "predictions"
    }

    fun savePrediction(prediction: Prediction) {

        val predictions =
            getPredictions().toMutableList()

        predictions.add(prediction)

        val jsonArray = JSONArray()

        predictions.forEach {

            val jsonObject = JSONObject()

            jsonObject.put("username", it.username)
            jsonObject.put("disease", it.disease)
            jsonObject.put("confidence", it.confidence)
            jsonObject.put("dateTime", it.dateTime)
            jsonObject.put("mode", it.mode)

            jsonArray.put(jsonObject)
        }

        prefs.edit()
            .putString(
                PREDICTIONS_KEY,
                jsonArray.toString()
            )
            .apply()
    }

    fun getPredictions(): List<Prediction> {

        val jsonString =
            prefs.getString(
                PREDICTIONS_KEY,
                null
            ) ?: return emptyList()

        return try {

            val jsonArray =
                JSONArray(jsonString)

            val predictions =
                mutableListOf<Prediction>()

            for (i in 0 until jsonArray.length()) {

                val item =
                    jsonArray.getJSONObject(i)

                predictions.add(
                    Prediction(
                        username =
                            item.getString("username"),

                        disease =
                            item.getString("disease"),

                        confidence =
                            item.getDouble("confidence")
                                .toFloat(),

                        dateTime =
                            item.getString("dateTime"),

                        mode =
                            item.getString("mode")
                    )
                )
            }

            predictions.reversed()

        } catch (e: Exception) {

            emptyList()
        }
    }

    fun clearPredictions() {

        prefs.edit()
            .remove(PREDICTIONS_KEY)
            .apply()
    }
}