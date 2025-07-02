package com.example.workrate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class LoginActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001

    // Replace with your actual web client ID from Google Cloud Console
    private val webClientId = "119851657498-4ptmed370cbe2b2blbihja4ege2q0p8g.apps.googleusercontent.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User already signed in, check profile
            checkUserProfileExists(currentUser.uid)
        }

        val googleSignInButton = findViewById<Button>(R.id.googleSignInButton)
        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        val createAccountLink = findViewById<TextView>(R.id.createAccountLink)
        createAccountLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val idToken = account.idToken

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser

                        if (user != null) {
                            val db = FirebaseFirestore.getInstance()
                            val userMap = hashMapOf(
                                "uid" to user.uid,
                                "name" to user.displayName,
                                "email" to user.email,
                                "photoUrl" to user.photoUrl?.toString()
                            )
                            // Merge so existing profile data is preserved
                            db.collection("users").document(user.uid)
                                .set(userMap, SetOptions.merge())
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Google user saved successfully")
                                    checkUserProfileExists(user.uid)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("Firestore", "Error saving Google user", e)
                                    Toast.makeText(this, "Failed to save user info", Toast.LENGTH_SHORT).show()
                                    checkUserProfileExists(user.uid)
                                }
                        } else {
                            // No Firebase user found, fallback to CompleteProfileActivity
                            startActivity(Intent(this, CompleteProfileActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Firebase Authentication failed", Toast.LENGTH_LONG).show()
                    }
                }
        } catch (e: ApiException) {
            Log.e("GOOGLE_SIGN_IN", "Sign-in failed: ${e.statusCode}", e)
            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkUserProfileExists(uid: String) {
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("users").document(uid)
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("LoginActivity", "User document data: ${document.data}")

                    val firstName = document.getString("firstName")
                    val lastName = document.getString("lastName")
                    val dob = document.getString("dateOfBirth")
                    val gender = document.getString("gender")

                    val profileComplete = !firstName.isNullOrEmpty() &&
                            !lastName.isNullOrEmpty() &&
                            !dob.isNullOrEmpty() &&
                            !gender.isNullOrEmpty()

                    if (profileComplete) {
                        Log.d("LoginActivity", "Profile complete. Going to MainActivity")
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Log.d("LoginActivity", "Profile incomplete. Going to CompleteProfileActivity")
                        startActivity(Intent(this, CompleteProfileActivity::class.java))
                    }
                } else {
                    Log.d("LoginActivity", "No user document found. Going to CompleteProfileActivity")
                    startActivity(Intent(this, CompleteProfileActivity::class.java))
                }
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("LoginActivity", "Failed to get user document", e)
                Toast.makeText(this, "Failed to verify profile status", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CompleteProfileActivity::class.java))
                finish()
            }
    }
}
