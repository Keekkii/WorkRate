package com.example.workrate

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.workrate.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load HomeFragment by default
        loadFragment(HomeFragment())

        setupBottomNav()
        setupAddButton()
    }

    private fun setupBottomNav() {
        binding.navHome.setOnClickListener {
            loadFragment(HomeFragment())
            updateSelectedNav("home")
        }
        binding.navSearch.setOnClickListener {
            // Replace with your SearchFragment
            loadFragment(SearchFragment())
            updateSelectedNav("search")
        }
        binding.navMap.setOnClickListener {
            // Replace with your MapFragment
            loadFragment(MapFragment())
            updateSelectedNav("map")
        }
        binding.navProfile.setOnClickListener {
            // Replace with your ProfileFragment
            loadFragment(ProfileFragment())
            updateSelectedNav("profile")
        }
    }

    private fun setupAddButton() {
        binding.navAdd.setOnClickListener {
            Toast.makeText(this, "Add button clicked!", Toast.LENGTH_SHORT).show()
            // TODO: Implement add button action here
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    private fun updateSelectedNav(selected: String) {
        // Reset all nav icons and labels to default (not selected)
        binding.iconHome.setColorFilter(getColor(R.color.gray))
        binding.labelHome.setTextColor(getColor(R.color.gray))
        binding.iconSearch.setColorFilter(getColor(R.color.gray))
        binding.labelSearch.setTextColor(getColor(R.color.gray))
        binding.iconMap.setColorFilter(getColor(R.color.gray))
        binding.labelMap.setTextColor(getColor(R.color.gray))
        binding.iconProfile.setColorFilter(getColor(R.color.gray))
        binding.labelProfile.setTextColor(getColor(R.color.gray))

        // Highlight the selected nav item (primary color)
        when(selected) {
            "home" -> {
                binding.iconHome.setColorFilter(getColor(R.color.primary))
                binding.labelHome.setTextColor(getColor(R.color.primary))
            }
            "search" -> {
                binding.iconSearch.setColorFilter(getColor(R.color.primary))
                binding.labelSearch.setTextColor(getColor(R.color.primary))
            }
            "map" -> {
                binding.iconMap.setColorFilter(getColor(R.color.primary))
                binding.labelMap.setTextColor(getColor(R.color.primary))
            }
            "profile" -> {
                binding.iconProfile.setColorFilter(getColor(R.color.primary))
                binding.labelProfile.setTextColor(getColor(R.color.primary))
            }
        }
    }
}
