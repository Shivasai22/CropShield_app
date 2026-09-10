package com.cropshield.app

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.get
import androidx.core.graphics.scale
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class TFLiteClassifier(
    private val context: Context
) {

    companion object {
        private const val MODEL_PATH = "models/mobilenetv2.tflite"
        private const val CLASS_NAMES_PATH = "models/class_names.txt"

        private const val INPUT_SIZE = 224
        private const val NUM_CLASSES = 38
    }

    private val interpreter: Interpreter
    private val classNames: List<String>

    init {
        interpreter = Interpreter(loadModel())

        classNames = loadClassNames()

        require(classNames.size == NUM_CLASSES) {
            "Expected $NUM_CLASSES classes, but found ${classNames.size}"
        }
    }

    /**
     * Load the TensorFlow Lite model from:
     * assets/models/mobilenetv2.tflite
     */
    private fun loadModel(): ByteBuffer {

        val fileDescriptor = context.assets.openFd(MODEL_PATH)

        val inputStream = FileInputStream(
            fileDescriptor.fileDescriptor
        )

        return inputStream.channel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }

    /**
     * Load the 38 PlantVillage class names from:
     * assets/models/class_names.txt
     */
    private fun loadClassNames(): List<String> {

        return context.assets
            .open(CLASS_NAMES_PATH)
            .bufferedReader()
            .readLines()
            .filter {
                it.isNotBlank()
            }
            .map {
                it.trim()
            }
    }

    /**
     * Run offline disease classification.
     *
     * Input:
     * Bitmap image
     *
     * Output:
     * Predicted class and confidence.
     */
    fun classify(bitmap: Bitmap): ClassificationResult {

        // Resize image to the model input size: 224 x 224
        val resizedBitmap = bitmap.scale(
            INPUT_SIZE,
            INPUT_SIZE
        )

        /*
         * Model input:
         * [1, 224, 224, 3]
         *
         * Data type:
         * Float32
         *
         * Pixel normalization:
         * 0.0 - 1.0
         */
        val inputBuffer = ByteBuffer.allocateDirect(
            INPUT_SIZE *
                    INPUT_SIZE *
                    3 *
                    4
        )

        inputBuffer.order(ByteOrder.nativeOrder())

        // Convert Bitmap pixels to Float32 RGB values
        for (y in 0 until INPUT_SIZE) {

            for (x in 0 until INPUT_SIZE) {

                val pixel = resizedBitmap[x, y]

                val red =
                    ((pixel shr 16) and 0xFF) / 255.0f

                val green =
                    ((pixel shr 8) and 0xFF) / 255.0f

                val blue =
                    (pixel and 0xFF) / 255.0f

                inputBuffer.putFloat(red)
                inputBuffer.putFloat(green)
                inputBuffer.putFloat(blue)
            }
        }

        /*
         * Model output:
         * [1, 38]
         */
        val output = Array(1) {
            FloatArray(NUM_CLASSES)
        }

        // Run TFLite inference
        interpreter.run(
            inputBuffer,
            output
        )

        // Find class with highest probability
        var bestIndex = 0
        var bestConfidence = output[0][0]

        for (i in 1 until NUM_CLASSES) {

            if (output[0][i] > bestConfidence) {

                bestConfidence = output[0][i]
                bestIndex = i
            }
        }

        val className =
            if (bestIndex < classNames.size) {
                classNames[bestIndex]
            } else {
                "Unknown"
            }

        return ClassificationResult(
            classIndex = bestIndex,
            className = className,
            confidence = bestConfidence
        )
    }

    /**
     * Release the TensorFlow Lite interpreter.
     */
    fun close() {
        interpreter.close()
    }
}

/**
 * Result returned by the offline classifier.
 */
data class ClassificationResult(

    val classIndex: Int,

    val className: String,

    /**
     * Confidence is between 0.0 and 1.0.
     *
     * Example:
     * 0.5798 = 57.98%
     */
    val confidence: Float
)