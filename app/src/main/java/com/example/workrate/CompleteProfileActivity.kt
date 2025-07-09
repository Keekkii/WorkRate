package com.example.workrate

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CompleteProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        // Fix for your insets code
        val rootView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.profileRoot)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get references to your views
        val firstNameEditText = findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEditText = findViewById<EditText>(R.id.lastNameEditText)
        val dobEditText = findViewById<EditText>(R.id.dobEditText)
        val genderRadioGroup = findViewById<RadioGroup>(R.id.genderRadioGroup)
        val confirmButton = findViewById<MaterialButton>(R.id.confirmButton)

        // Show DatePickerDialog on DOB click
        dobEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d.%02d.%04d", selectedDay, selectedMonth + 1, selectedYear)
                dobEditText.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        confirmButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val dob = dobEditText.text.toString().trim()
            val genderId = genderRadioGroup.checkedRadioButtonId
            val gender = if (genderId != -1) {
                findViewById<RadioButton>(genderId).text.toString()
            } else {
                ""
            }

            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = FirebaseFirestore.getInstance()
            val userData = hashMapOf(
                "uid" to user.uid,
                "email" to user.email,
                "firstName" to firstName,
                "lastName" to lastName,
                "dateOfBirth" to dob,
                "gender" to gender
            )

            db.collection("users").document(user.uid).set(userData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ChooseRoleActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save profile: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
