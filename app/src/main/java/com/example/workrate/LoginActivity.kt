package com.example.workrate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001

    private val webClientId = "119851657498-4ptmed370cbe2b2blbihja4ege2q0p8g.apps.googleusercontent.com"

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var signInButton: MaterialButton
    private lateinit var googleSignInButton: MaterialButton
    private lateinit var createAccountLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signInButton = findViewById(R.id.signInButton)
        googleSignInButton = findViewById(R.id.googleSignInButton)
        createAccountLink = findViewById(R.id.createAccountLink)

        createAccountLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            checkUserProfileExists(currentUser.uid)
            return
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.let { checkUserProfileExists(it.uid) }
                    } else {
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val idToken = account.idToken

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.let {
                            val db = FirebaseFirestore.getInstance()
                            val userRef = db.collection("users").document(user.uid)

                            userRef.get().addOnSuccessListener { document ->
                                if (!document.exists()) {
                                    val userMap = hashMapOf(
                                        "uid" to user.uid,
                                        "name" to user.displayName,
                                        "email" to user.email,
                                        "photoUrl" to user.photoUrl?.toString()
                                    )
                                    userRef.set(userMap)
                                        .addOnSuccessListener { checkUserProfileExists(user.uid) }
                                        .addOnFailureListener { checkUserProfileExists(user.uid) }
                                } else {
                                    checkUserProfileExists(user.uid)
                                }
                            }.addOnFailureListener {
                                checkUserProfileExists(user.uid)
                            }
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
        val userRef = db.collection("users").document(uid)

        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val dateOfBirth = document.getString("dateOfBirth")
                val role = document.getString("role")

                when {
                    dateOfBirth.isNullOrEmpty() -> {
                        startActivity(Intent(this, CompleteProfileActivity::class.java))
                    }
                    role.isNullOrEmpty() -> {
                        startActivity(Intent(this, ChooseRoleActivity::class.java))
                    }
                    role == "looking_for_work" -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                    role == "looking_for_workers" -> {
                        startActivity(Intent(this, HomeBusinessActivity::class.java))
                    }
                    else -> {
                        startActivity(Intent(this, ChooseRoleActivity::class.java))
                    }
                }
                finish()
            } else {
                startActivity(Intent(this, CompleteProfileActivity::class.java))
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CompleteProfileActivity::class.java))
            finish()
        }
    }
}
