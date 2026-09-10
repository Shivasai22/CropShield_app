package com.cropshield.app

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class AuthManager(context: Context) {

    private val prefs =
        context.getSharedPreferences(
            "CropShieldAuth",
            Context.MODE_PRIVATE
        )

    private val firebaseAuth =
        FirebaseAuth.getInstance()

    /*
     * ---------------------------------------------------------
     * SIGN UP
     * ---------------------------------------------------------
     *
     * Firebase creates the account.
     *
     * Email is used as the username.
     */
    fun signUp(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {

        if (email.isBlank()) {

            onResult(
                false,
                "Enter your email"
            )

            return
        }

        if (password.isBlank()) {

            onResult(
                false,
                "Enter your password"
            )

            return
        }

        if (password.length < 6) {

            onResult(
                false,
                "Password must be at least 6 characters"
            )

            return
        }

        firebaseAuth
            .createUserWithEmailAndPassword(
                email.trim(),
                password
            )
            .addOnSuccessListener {

                /*
                 * Save email locally so the app can display
                 * the username while offline.
                 */
                prefs.edit()
                    .putString(
                        "username",
                        email.trim()
                    )
                    .putBoolean(
                        "loggedIn",
                        true
                    )
                    .apply()

                onResult(
                    true,
                    "Account created successfully"
                )
            }
            .addOnFailureListener { exception ->

                onResult(
                    false,
                    exception.message
                        ?: "Account creation failed"
                )
            }
    }

    /*
     * ---------------------------------------------------------
     * LOGIN
     * ---------------------------------------------------------
     */

    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {

        if (email.isBlank()) {

            onResult(
                false,
                "Enter your email"
            )

            return
        }

        if (password.isBlank()) {

            onResult(
                false,
                "Enter your password"
            )

            return
        }

        firebaseAuth
            .signInWithEmailAndPassword(
                email.trim(),
                password
            )
            .addOnSuccessListener {

                /*
                 * Save login information locally.
                 *
                 * This allows CropShield to remember the
                 * signed-in user on the device.
                 */
                prefs.edit()
                    .putString(
                        "username",
                        email.trim()
                    )
                    .putBoolean(
                        "loggedIn",
                        true
                    )
                    .apply()

                onResult(
                    true,
                    "Login successful"
                )
            }
            .addOnFailureListener { exception ->

                onResult(
                    false,
                    exception.message
                        ?: "Login failed"
                )
            }
    }

    /*
     * ---------------------------------------------------------
     * LOGOUT
     * ---------------------------------------------------------
     */

    fun logout() {

        /*
         * Sign out from Firebase.
         */
        firebaseAuth.signOut()

        /*
         * Sign out locally.
         */
        prefs.edit()
            .putBoolean(
                "loggedIn",
                false
            )
            .apply()
    }

    /*
     * ---------------------------------------------------------
     * SET LOGIN STATE
     * ---------------------------------------------------------
     */

    fun setLoggedIn(
        value: Boolean
    ) {

        prefs.edit()
            .putBoolean(
                "loggedIn",
                value
            )
            .apply()
    }

    /*
     * ---------------------------------------------------------
     * CHECK LOGIN
     * ---------------------------------------------------------
     */

    fun isLoggedIn(): Boolean {

        /*
         * Firebase is the source of truth when available.
         */
        if (firebaseAuth.currentUser != null) {
            return true
        }

        /*
         * Fall back to local state.
         *
         * This keeps the app usable offline.
         */
        return prefs.getBoolean(
            "loggedIn",
            false
        )
    }

    /*
     * ---------------------------------------------------------
     * GET USERNAME
     * ---------------------------------------------------------
     */

    fun getUsername(): String {

        return prefs.getString(
            "username",
            firebaseAuth.currentUser?.email ?: ""
        ) ?: ""
    }

    /*
     * ---------------------------------------------------------
     * GET FIREBASE USER ID
     * ---------------------------------------------------------
     */

    fun getFirebaseUserId(): String? {

        return firebaseAuth
            .currentUser
            ?.uid
    }

    /*
     * ---------------------------------------------------------
     * CHECK FIREBASE LOGIN
     * ---------------------------------------------------------
     */

    fun isFirebaseLoggedIn(): Boolean {

        return firebaseAuth.currentUser != null
    }
}