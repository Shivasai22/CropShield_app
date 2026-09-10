package com.cropshield.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.cropshield.app.ui.theme.CropShieldTheme

class MainActivity : ComponentActivity() {

    private lateinit var classifier: TFLiteClassifier
    private lateinit var authManager: AuthManager
    private lateinit var predictionStorage: PredictionStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        classifier = TFLiteClassifier(this)

        authManager = AuthManager(this)

        predictionStorage = PredictionStorage(this)

        setContent {

            CropShieldTheme {

                var loggedIn by remember {
                    mutableStateOf(
                        authManager.isLoggedIn()
                    )
                }

                var screen by remember {
                    mutableStateOf("home")
                }

                var selectedAdvice by remember {
                    mutableStateOf<DiseaseAdvice?>(null)
                }

                /*
                 * Detection mode
                 *
                 * Default = OFFLINE
                 */
                var detectionMode by remember {
                    mutableStateOf(
                        DetectionMode.OFFLINE
                    )
                }

                if (!loggedIn) {

                    AuthScreen(
                        authManager = authManager,

                        onLoginSuccess = {

                            loggedIn = true

                            screen = "home"
                        }
                    )

                } else {

                    when (screen) {

                        /*
                         * HOME
                         */
                        "home" -> {

                            CropShieldHomeScreenWithMode(
                                username =
                                    authManager.getUsername(),

                                selectedMode =
                                    detectionMode,

                                onModeSelected = { mode ->

                                    detectionMode = mode
                                },

                                onScanPlant = {

                                    screen = "scan"
                                },

                                onHistory = {

                                    screen = "history"
                                },

                                onLogout = {

                                    authManager.logout()

                                    loggedIn = false

                                    screen = "home"
                                }
                            )
                        }

                        /*
                         * DETECTION
                         */
                        "scan" -> {

                            CropShieldDetectionScreen(
                                username =
                                    authManager.getUsername(),

                                classifier =
                                    classifier,

                                predictionStorage =
                                    predictionStorage,

                                mode =
                                    detectionMode,

                                onBack = {

                                    screen = "home"
                                },

                                onViewAdvice = { advice ->

                                    selectedAdvice =
                                        advice

                                    screen = "advice"
                                }
                            )
                        }

                        /*
                         * HISTORY
                         */
                        "history" -> {

                            PredictionHistoryScreen(
                                predictions =
                                    predictionStorage
                                        .getPredictions(),

                                onBack = {

                                    screen = "home"
                                },

                                onClear = {

                                    predictionStorage
                                        .clearPredictions()

                                    screen = "history"
                                }
                            )
                        }

                        /*
                         * DISEASE ADVICE
                         */
                        "advice" -> {

                            selectedAdvice?.let { advice ->

                                DiseaseAdviceScreen(
                                    advice = advice,

                                    onBack = {

                                        screen = "scan"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {

        classifier.close()

        super.onDestroy()
    }
}


/*
 * ============================================================
 * HOME SCREEN WITH MODE SELECTION
 * ============================================================
 */

@Composable
fun CropShieldHomeScreenWithMode(
    username: String,
    selectedMode: DetectionMode,
    onModeSelected: (DetectionMode) -> Unit,
    onScanPlant: () -> Unit,
    onHistory: () -> Unit,
    onLogout: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "CropShield"
        )

        Text(
            text = "Welcome, $username",

            modifier =
                Modifier.padding(
                    top = 12.dp
                )
        )

        /*
         * Mode title
         */
        Text(
            text = "Detection Mode",

            modifier =
                Modifier.padding(
                    top = 24.dp
                )
        )

        /*
         * OFFLINE MODE
         */
        Button(
            onClick = {

                onModeSelected(
                    DetectionMode.OFFLINE
                )
            },

            modifier =
                Modifier.padding(
                    top = 12.dp
                )
        ) {

            Text(
                text =
                    if (
                        selectedMode ==
                        DetectionMode.OFFLINE
                    ) {
                        "✓ Offline Mode"
                    } else {
                        "Offline Mode"
                    }
            )
        }

        /*
         * ONLINE MODE
         */
        Button(
            onClick = {

                onModeSelected(
                    DetectionMode.ONLINE
                )
            },

            modifier =
                Modifier.padding(
                    top = 8.dp
                )
        ) {

            Text(
                text =
                    if (
                        selectedMode ==
                        DetectionMode.ONLINE
                    ) {
                        "✓ Online Mode"
                    } else {
                        "Online Mode"
                    }
            )
        }

        /*
         * Selected mode
         */
        Text(
            text =
                "Selected: ${selectedMode.name}",

            modifier =
                Modifier.padding(
                    top = 12.dp
                )
        )

        /*
         * Scan button
         */
        Button(
            onClick = {

                onScanPlant()
            },

            modifier =
                Modifier.padding(
                    top = 20.dp
                )
        ) {

            Text(
                text = "Scan Plant"
            )
        }

        /*
         * History
         */
        Button(
            onClick = {

                onHistory()
            },

            modifier =
                Modifier.padding(
                    top = 10.dp
                )
        ) {

            Text(
                text = "Prediction History"
            )
        }

        /*
         * Logout
         */
        Button(
            onClick = {

                onLogout()
            },

            modifier =
                Modifier.padding(
                    top = 10.dp
                )
        ) {

            Text(
                text = "Logout"
            )
        }
    }
}


/*
 * ============================================================
 * DETECTION SCREEN
 * ============================================================
 */

@Composable
fun CropShieldDetectionScreen(
    username: String,
    classifier: TFLiteClassifier,
    predictionStorage: PredictionStorage,
    mode: DetectionMode,
    onBack: () -> Unit,
    onViewAdvice: (DiseaseAdvice) -> Unit
) {

    val context =
        LocalContext.current

    var result by remember {

        mutableStateOf(
            "Ready for detection"
        )
    }

    var lastAdvice by remember {

        mutableStateOf<DiseaseAdvice?>(null)
    }

    var lastDisease by remember {

        mutableStateOf("")
    }

    var lastConfidence by remember {

        mutableStateOf(0f)
    }

    var lastDateTime by remember {

        mutableStateOf("")
    }

    var lastMode by remember {

        mutableStateOf("")
    }

    /*
     * ========================================================
     * CAMERA LAUNCHER
     * ========================================================
     */

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .TakePicturePreview()
        ) { bitmap ->

            if (bitmap == null) {

                result =
                    "Camera capture cancelled"

                lastAdvice = null

                return@rememberLauncherForActivityResult
            }

            /*
             * ------------------------------------------------
             * ONLINE MODE
             * ------------------------------------------------
             *
             * Actual online AI API will be connected
             * in the next step.
             */

            if (
                mode ==
                DetectionMode.ONLINE
            ) {

                result =
                    "Online Mode selected.\n\n" +
                            "Online prediction service is not connected yet."

                lastAdvice = null

                return@rememberLauncherForActivityResult
            }

            /*
             * ------------------------------------------------
             * OFFLINE MODE
             * ------------------------------------------------
             */

            try {

                val prediction =
                    classifier.classify(bitmap)

                val confidence =
                    prediction.confidence * 100

                val dateTime =
                    java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        java.util.Locale
                            .getDefault()
                    ).format(
                        java.util.Date()
                    )

                /*
                 * Save latest result
                 */
                lastDisease =
                    prediction.className

                lastConfidence =
                    confidence.toFloat()

                lastDateTime =
                    dateTime

                lastMode =
                    "OFFLINE"

                /*
                 * Save locally
                 */
                predictionStorage.savePrediction(

                    Prediction(

                        username =
                            username,

                        disease =
                            prediction.className,

                        confidence =
                            confidence.toFloat(),

                        dateTime =
                            dateTime,

                        mode =
                            "OFFLINE"
                    )
                )

                /*
                 * Disease advice
                 */
                val advice =
                    DiseaseAdviceRepository
                        .getAdvice(
                            prediction.className
                        )

                lastAdvice =
                    advice

                result =
                    "Disease: " +
                            prediction.className +
                            "\n\n" +

                            "Confidence: %.2f%%\n\n"
                                .format(
                                    confidence
                                ) +

                            "Mode: OFFLINE\n\n" +

                            "Prediction saved locally"

            } catch (e: Exception) {

                result =
                    "Prediction failed:\n" +
                            "${e.message}"

                lastAdvice = null
            }
        }


    /*
     * ========================================================
     * CAMERA PERMISSION
     * ========================================================
     */

    val permissionLauncher =
        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts
                    .RequestPermission()

        ) { granted ->

            if (granted) {

                cameraLauncher.launch(
                    null
                )

            } else {

                result =
                    "Camera permission is required"
            }
        }


    /*
     * ========================================================
     * USER INTERFACE
     * ========================================================
     */

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        /*
         * Title
         */
        Text(
            text = "Plant Detection"
        )

        /*
         * Username
         */
        Text(

            text =
                "User: $username",

            modifier =
                Modifier.padding(
                    top = 8.dp
                )
        )

        /*
         * Mode
         */
        Text(

            text =
                "Mode: ${mode.name}",

            modifier =
                Modifier.padding(
                    top = 8.dp
                )
        )

        /*
         * Capture button
         */
        Button(

            onClick = {

                val permission =
                    ContextCompat
                        .checkSelfPermission(

                            context,

                            Manifest.permission
                                .CAMERA
                        )

                if (
                    permission ==
                    PackageManager
                        .PERMISSION_GRANTED
                ) {

                    cameraLauncher
                        .launch(null)

                } else {

                    permissionLauncher
                        .launch(
                            Manifest.permission
                                .CAMERA
                        )
                }
            },

            modifier =
                Modifier.padding(
                    top = 20.dp
                )
        ) {

            Text(
                text =
                    "Capture Plant Image"
            )
        }

        /*
         * Prediction result
         */
        Text(

            text = result,

            modifier =
                Modifier.padding(
                    top = 20.dp
                )
        )

        /*
         * Disease advice
         */
        if (
            lastAdvice != null
        ) {

            Button(

                onClick = {

                    lastAdvice?.let { advice ->

                        onViewAdvice(
                            advice
                        )
                    }
                },

                modifier =
                    Modifier.padding(
                        top = 12.dp
                    )
            ) {

                Text(
                    text =
                        "View Disease Advice"
                )
            }
        }

        /*
         * PDF report
         */
        if (
            lastAdvice != null
        ) {

            Button(

                onClick = {

                    lastAdvice?.let { advice ->

                        try {

                            val pdfFile =
                                PdfReportGenerator
                                    .generateReport(

                                        context =
                                            context,

                                        username =
                                            username,

                                        disease =
                                            lastDisease,

                                        confidence =
                                            lastConfidence,

                                        dateTime =
                                            lastDateTime,

                                        mode =
                                            lastMode,

                                        advice =
                                            advice
                                    )

                            val uri =
                                FileProvider
                                    .getUriForFile(

                                        context,

                                        "${context.packageName}.fileprovider",

                                        pdfFile
                                    )

                            val intent =
                                Intent(
                                    Intent.ACTION_VIEW
                                ).apply {

                                    setDataAndType(

                                        uri,

                                        "application/pdf"
                                    )

                                    addFlags(

                                        Intent
                                            .FLAG_GRANT_READ_URI_PERMISSION
                                    )
                                }

                            context.startActivity(
                                intent
                            )

                        } catch (
                            e: Exception
                        ) {

                            Toast
                                .makeText(

                                    context,

                                    "Unable to open PDF: ${e.message}",

                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                },

                modifier =
                    Modifier.padding(
                        top = 12.dp
                    )
            ) {

                Text(
                    text =
                        "Generate PDF Report"
                )
            }
        }

        /*
         * Back
         */
        Button(

            onClick =
                onBack,

            modifier =
                Modifier.padding(
                    top = 20.dp
                )
        ) {

            Text(
                text =
                    "Back to Home"
            )
        }
    }
}