package com.example.workrate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.workrate.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Debug build check - ADD THIS LINE INSIDE onCreate
        Log.d("BUILD_TYPE", "Is debug build? ${BuildConfig.DEBUG}")

        // Configure Google Sign-In with your new Client ID
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("119851657498-bgp74hrd0c1hklbvirh5k94h0i2vcbc1.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Google Sign-In button click
        binding.googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }

        // Email/password login (simple demo)
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email == "user@example.com" && password == "1234") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        // Optional: Create account link
        binding.createAccountLink.setOnClickListener {
            Toast.makeText(this, "Registration not implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSignInResult(task: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            // Signed in successfully, go to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: ApiException) {
            Log.e("GOOGLE_SIGNIN", "Google sign-in failed")
            Log.e("GOOGLE_SIGNIN", "Status code: ${e.statusCode}")
            Log.e("GOOGLE_SIGNIN", "Status message: ${e.message}")
            Log.e("GOOGLE_SIGNIN", "Stack trace:", e)
            Toast.makeText(
                this,
                "Google sign-in failed: ${e.statusCode}\n${e.message}",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Log.e("GOOGLE_SIGNIN", "Unexpected error during sign-in", e)
            Toast.makeText(
                this,
                "Unexpected error: ${e.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
