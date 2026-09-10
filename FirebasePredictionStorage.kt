package com.cropshield.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FirebasePredictionStorage {

    private val auth =
        FirebaseAuth.getInstance()

    private val database =
        FirebaseDatabase.getInstance(
            "https://cropshield-ai-87a46-default-rtdb.asia-southeast1.firebasedatabase.app"
        )

    fun savePrediction(
        prediction: Prediction,
        onResult: (Boolean, String?) -> Unit
    ) {

        val firebaseUser =
            auth.currentUser

        if (firebaseUser == null) {

            onResult(
                false,
                "No Firebase user is signed in"
            )

            return
        }

        val userId =
            firebaseUser.uid

        val predictionData =
            hashMapOf<String, Any>(
                "username" to prediction.username,
                "disease" to prediction.disease,
                "confidence" to prediction.confidence,
                "dateTime" to prediction.dateTime,
                "mode" to prediction.mode
            )

        database
            .getReference("users")
            .child(userId)
            .child("predictions")
            .push()
            .setValue(predictionData)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    onResult(
                        true,
                        null
                    )

                } else {

                    onResult(
                        false,
                        task.exception?.message
                            ?: "Database write failed"
                    )
                }
            }
    }

    /*
     * Get all predictions from Firebase
     */
    fun getPredictions(
        onResult: (List<Prediction>, String?) -> Unit
    ) {

        val firebaseUser =
            auth.currentUser

        if (firebaseUser == null) {

            onResult(
                emptyList(),
                "No Firebase user is signed in"
            )

            return
        }

        val userId =
            firebaseUser.uid

        database
            .getReference("users")
            .child(userId)
            .child("predictions")
            .get()
            .addOnSuccessListener { snapshot ->

                val predictions =
                    mutableListOf<Prediction>()

                for (
                child in snapshot.children
                ) {

                    val username =
                        child
                            .child("username")
                            .getValue(String::class.java)
                            ?: ""

                    val disease =
                        child
                            .child("disease")
                            .getValue(String::class.java)
                            ?: ""

                    val confidence =
                        child
                            .child("confidence")
                            .getValue(Float::class.java)
                            ?: child
                                .child("confidence")
                                .getValue(Double::class.java)
                                ?.toFloat()
                            ?: 0f

                    val dateTime =
                        child
                            .child("dateTime")
                            .getValue(String::class.java)
                            ?: ""

                    val mode =
                        child
                            .child("mode")
                            .getValue(String::class.java)
                            ?: "CLOUD"

                    predictions.add(
                        Prediction(
                            username = username,
                            disease = disease,
                            confidence = confidence,
                            dateTime = dateTime,
                            mode = mode
                        )
                    )
                }

                onResult(
                    predictions,
                    null
                )
            }
            .addOnFailureListener { exception ->

                onResult(
                    emptyList(),
                    exception.message
                        ?: "Unable to load cloud predictions"
                )
            }
    }
}