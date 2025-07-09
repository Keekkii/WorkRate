package com.example.workrate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImageView = view.findViewById(R.id.profileImageView)
        nameTextView = view.findViewById(R.id.nameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)

        loadUserData()

        return view
    }

    private fun loadUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = currentUser.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val fullName = when {
                        document.contains("firstName") && document.contains("lastName") -> {
                            val firstName = document.getString("firstName") ?: ""
                            val lastName = document.getString("lastName") ?: ""
                            "$firstName $lastName"
                        }
                        document.contains("name") -> {
                            document.getString("name") ?: "No name"
                        }
                        else -> "No name"
                    }

                    val email = document.getString("email") ?: "No email"
                    val photoUrl = document.getString("photoUrl") ?: ""
                    val gender = document.getString("gender")?.lowercase() ?: ""

                    nameTextView.text = fullName
                    emailTextView.text = email

                    if (photoUrl.isNotEmpty()) {
                        Glide.with(this)
                            .load(photoUrl)
                            .circleCrop()
                            .placeholder(R.drawable.ellipse_679)
                            .into(profileImageView)
                    } else {
                        val drawableRes = when (gender) {
                            "male" -> R.drawable.manprof
                            "female" -> R.drawable.femaleprof
                            else -> R.drawable.ellipse_679
                        }
                        profileImageView.setImageResource(drawableRes)
                    }
                } else {
                    Toast.makeText(requireContext(), "User document not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to load user data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
