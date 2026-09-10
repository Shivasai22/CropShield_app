package com.cropshield.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    authManager: AuthManager,
    onLoginSuccess: () -> Unit
) {

    /*
     * ---------------------------------------------------------
     * Email
     * ---------------------------------------------------------
     */

    var email by remember {

        mutableStateOf("")
    }

    /*
     * ---------------------------------------------------------
     * Password
     * ---------------------------------------------------------
     */

    var password by remember {

        mutableStateOf("")
    }

    /*
     * ---------------------------------------------------------
     * Status message
     * ---------------------------------------------------------
     */

    var message by remember {

        mutableStateOf("")
    }

    /*
     * ---------------------------------------------------------
     * Loading state
     * ---------------------------------------------------------
     */

    var loading by remember {

        mutableStateOf(false)
    }

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
         * -----------------------------------------------------
         * App title
         * -----------------------------------------------------
         */

        Text(
            text = "CropShield"
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        /*
         * -----------------------------------------------------
         * Email field
         * -----------------------------------------------------
         */

        OutlinedTextField(

            value =
                email,

            onValueChange = {

                email = it
            },

            label = {

                Text(
                    text = "Email"
                )
            },

            singleLine = true
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        /*
         * -----------------------------------------------------
         * Password field
         * -----------------------------------------------------
         */

        OutlinedTextField(

            value =
                password,

            onValueChange = {

                password = it
            },

            label = {

                Text(
                    text = "Password"
                )
            },

            visualTransformation =
                PasswordVisualTransformation(),

            singleLine = true
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        /*
         * -----------------------------------------------------
         * SIGN IN
         * -----------------------------------------------------
         */

        Button(

            enabled =
                !loading,

            onClick = {

                loading = true

                message =
                    "Signing in..."

                authManager.login(

                    email =
                        email,

                    password =
                        password

                ) { success, resultMessage ->

                    loading = false

                    message =
                        resultMessage

                    if (success) {

                        onLoginSuccess()
                    }
                }
            }
        ) {

            Text(
                text =
                    if (loading)
                        "Please wait..."
                    else
                        "Sign In"
            )
        }

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        /*
         * -----------------------------------------------------
         * SIGN UP
         * -----------------------------------------------------
         */

        Button(

            enabled =
                !loading,

            onClick = {

                loading = true

                message =
                    "Creating account..."

                authManager.signUp(

                    email =
                        email,

                    password =
                        password

                ) { success, resultMessage ->

                    loading = false

                    message =
                        resultMessage

                    if (success) {

                        onLoginSuccess()
                    }
                }
            }
        ) {

            Text(
                text = "Sign Up"
            )
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        /*
         * -----------------------------------------------------
         * Status message
         * -----------------------------------------------------
         */

        Text(
            text = message
        )
    }
}