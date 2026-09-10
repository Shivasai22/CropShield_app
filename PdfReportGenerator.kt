package com.cropshield.app

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

object PdfReportGenerator {

    fun generateReport(
        context: Context,
        username: String,
        disease: String,
        confidence: Float,
        dateTime: String,
        mode: String,
        advice: DiseaseAdvice
    ): File {

        val document = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(
            595,
            842,
            1
        ).create()

        val page = document.startPage(pageInfo)

        val canvas = page.canvas

        val titlePaint = Paint().apply {
            textSize = 24f
            isFakeBoldText = true
        }

        val headingPaint = Paint().apply {
            textSize = 16f
            isFakeBoldText = true
        }

        val bodyPaint = Paint().apply {
            textSize = 12f
        }

        var y = 50f

        canvas.drawText(
            "CropShield Prediction Report",
            40f,
            y,
            titlePaint
        )

        y += 40f

        canvas.drawText(
            "User: $username",
            40f,
            y,
            bodyPaint
        )

        y += 22f

        canvas.drawText(
            "Date: $dateTime",
            40f,
            y,
            bodyPaint
        )

        y += 22f

        canvas.drawText(
            "Mode: $mode",
            40f,
            y,
            bodyPaint
        )

        y += 35f

        canvas.drawText(
            "Prediction",
            40f,
            y,
            headingPaint
        )

        y += 25f

        canvas.drawText(
            "Disease: $disease",
            40f,
            y,
            bodyPaint
        )

        y += 22f

        canvas.drawText(
            "Confidence: %.2f%%".format(confidence),
            40f,
            y,
            bodyPaint
        )

        y += 35f

        canvas.drawText(
            "Disease Description",
            40f,
            y,
            headingPaint
        )

        y += 22f

        y = drawWrappedText(
            canvas,
            advice.description,
            bodyPaint,
            y
        )

        y += 15f

        canvas.drawText(
            "Symptoms",
            40f,
            y,
            headingPaint
        )

        y += 22f

        y = drawWrappedText(
            canvas,
            advice.symptoms,
            bodyPaint,
            y
        )

        y += 15f

        canvas.drawText(
            "Recommended Action",
            40f,
            y,
            headingPaint
        )

        y += 22f

        y = drawWrappedText(
            canvas,
            advice.recommendation,
            bodyPaint,
            y
        )

        y += 15f

        canvas.drawText(
            "Prevention",
            40f,
            y,
            headingPaint
        )

        y += 22f

        drawWrappedText(
            canvas,
            advice.prevention,
            bodyPaint,
            y
        )

        document.finishPage(page)

        val reportsDirectory =
            File(
                context.getExternalFilesDir(null),
                "CropShieldReports"
            )

        if (!reportsDirectory.exists()) {
            reportsDirectory.mkdirs()
        }

        val safeDisease =
            disease
                .replace(
                    Regex("[^A-Za-z0-9._-]"),
                    "_"
                )

        val fileName =
            "CropShield_${safeDisease}_${System.currentTimeMillis()}.pdf"

        val file =
            File(
                reportsDirectory,
                fileName
            )

        FileOutputStream(file).use { outputStream ->

            document.writeTo(outputStream)
        }

        document.close()

        return file
    }

    private fun drawWrappedText(
        canvas: android.graphics.Canvas,
        text: String,
        paint: Paint,
        startY: Float
    ): Float {

        val maxWidth = 510f
        val words = text.split(" ")

        var line = ""
        var y = startY

        for (word in words) {

            val testLine =
                if (line.isEmpty()) {
                    word
                } else {
                    "$line $word"
                }

            if (
                paint.measureText(testLine) > maxWidth
            ) {

                canvas.drawText(
                    line,
                    40f,
                    y,
                    paint
                )

                y += 18f
                line = word

            } else {

                line = testLine
            }
        }

        if (line.isNotEmpty()) {

            canvas.drawText(
                line,
                40f,
                y,
                paint
            )

            y += 18f
        }

        return y
    }
}