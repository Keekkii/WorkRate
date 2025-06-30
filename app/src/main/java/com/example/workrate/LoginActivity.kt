package com.example.workrate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.workrate.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle login button click
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Simple validation (replace with real authentication)
            if (email == "user@example.com" && password == "1234") {
                // Login successful, go to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Prevent back navigation to login
            } else {
                // Show error
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        // Example: Handle "Create one" click (optional)
        binding.createAccountLink.setOnClickListener {
            Toast.makeText(this, "Registration not implemented", Toast.LENGTH_SHORT).show()
            // Or startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
