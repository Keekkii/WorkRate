package com.example.workrate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.workrate.databinding.ActivityChooseRoleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChooseRoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseRoleBinding
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChooseRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.confirmButton.setOnClickListener {
            val selectedRadioId = binding.roleRadioGroup.checkedRadioButtonId
            if (selectedRadioId == -1) {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRole = when (selectedRadioId) {
                R.id.radioLookingForWork -> "looking_for_work"
                R.id.radioLookingForWorkers -> "looking_for_workers"
                else -> ""
            }

            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val uid = currentUser.uid
            val userData = mapOf("role" to selectedRole)

            // Update only the role field in existing user document
            firestore.collection("users")
                .document(uid)
                .update(userData)
                .addOnSuccessListener {
                    // Successfully saved - move to HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save role. Try again.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
