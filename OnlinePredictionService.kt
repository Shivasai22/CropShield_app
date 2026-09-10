package com.cropshield.app

import android.graphics.Bitmap

class OnlinePredictionService {

    fun predict(
        bitmap: Bitmap,
        onSuccess: (DiseasePrediction) -> Unit,
        onError: (String) -> Unit
    ) {

        /*
         * Online AI API will be connected here.
         *
         * The bitmap will eventually be uploaded
         * to your online prediction server.
         */

        onError(
            "Online prediction server is not connected yet"
        )
    }
}