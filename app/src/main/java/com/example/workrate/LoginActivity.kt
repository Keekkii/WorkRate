package com.example.workrate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class LoginActivity : ComponentActivity() {

    // Your web client ID from Google Cloud Console
    private val webClientId = "119851657498-4ptmed370cbe2b2blbihja4ege2q0p8g.apps.googleusercontent.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val googleSignInButton = findViewById<Button>(R.id.googleSignInButton)
        googleSignInButton.setOnClickListener {
            // Directly start Google sign-in
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // false = allow all accounts on device
            .setServerClientId(webClientId)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(this)

        lifecycleScope.launch {
            try {
                val response: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity
                )
                handleSignIn(response)
            } catch (e: GetCredentialException) {
                Log.e("CREDENTIAL_MANAGER", "Sign-in failed", e)
                Toast.makeText(
                    this@LoginActivity,
                    "Sign-in failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun handleSignIn(response: GetCredentialResponse) {
        val credential = response.credential
        if (credential is androidx.credentials.CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val displayName = googleIdTokenCredential.displayName
                val email = googleIdTokenCredential.id

                Log.d("CREDENTIAL_MANAGER", "ID Token: $idToken")
                Log.d("CREDENTIAL_MANAGER", "Name: $displayName")
                Log.d("CREDENTIAL_MANAGER", "Email: $email")

                // TODO: Send idToken to backend for verification (if needed)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e("CREDENTIAL_MANAGER", "Invalid Google ID token", e)
            }
        } else {
            Log.e("CREDENTIAL_MANAGER", "Unexpected credential type: ${credential::class.java}")
        }
    }
}
