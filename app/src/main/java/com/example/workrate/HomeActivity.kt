package com.example.workrate

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {

    private lateinit var navHome: LinearLayout
    private lateinit var navSearch: LinearLayout
    private lateinit var navMap: LinearLayout
    private lateinit var navProfile: LinearLayout
    private lateinit var navAdd: ImageView

    private lateinit var homeIconBg: FrameLayout
    private lateinit var searchIconBg: FrameLayout
    private lateinit var mapIconBg: FrameLayout
    private lateinit var profileIconBg: FrameLayout

    private lateinit var iconHome: ImageView
    private lateinit var iconSearch: ImageView
    private lateinit var iconMap: ImageView
    private lateinit var iconProfile: ImageView

    private lateinit var labelHome: TextView
    private lateinit var labelSearch: TextView
    private lateinit var labelMap: TextView
    private lateinit var labelProfile: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)  // your layout file name here

        // Initialize views with findViewById
        navHome = findViewById(R.id.nav_home)
        navSearch = findViewById(R.id.nav_search)
        navMap = findViewById(R.id.nav_map)
        navProfile = findViewById(R.id.nav_profile)
        navAdd = findViewById(R.id.nav_add)

        homeIconBg = findViewById(R.id.home_icon_bg)
        searchIconBg = findViewById(R.id.search_icon_bg)
        mapIconBg = findViewById(R.id.map_icon_bg)
        profileIconBg = findViewById(R.id.profile_icon_bg)

        iconHome = findViewById(R.id.icon_home)
        iconSearch = findViewById(R.id.icon_search)
        iconMap = findViewById(R.id.icon_map)
        iconProfile = findViewById(R.id.icon_profile)

        labelHome = findViewById(R.id.label_home)
        labelSearch = findViewById(R.id.label_search)
        labelMap = findViewById(R.id.label_map)
        labelProfile = findViewById(R.id.label_profile)

        // Load HomeFragment by default
        loadFragment(HomeFragment())
        updateSelectedNav("home")

        setupBottomNav()
        setupAddButton()
    }

    private fun setupBottomNav() {
        navHome.setOnClickListener {
            loadFragment(HomeFragment())
            updateSelectedNav("home")
        }
        navSearch.setOnClickListener {
            loadFragment(SearchFragment())
            updateSelectedNav("search")
        }
        navMap.setOnClickListener {
            loadFragment(MapFragment())
            updateSelectedNav("map")
        }
        navProfile.setOnClickListener {
            loadFragment(ProfileFragment())
            updateSelectedNav("profile")
        }
    }

    private fun setupAddButton() {
        navAdd.setOnClickListener {
            Toast.makeText(this, "Add button clicked!", Toast.LENGTH_SHORT).show()
            // TODO: Implement add button action here, e.g., open AddFragment or start new Activity
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun updateSelectedNav(selected: String) {
        // Reset backgrounds
        homeIconBg.isSelected = false
        searchIconBg.isSelected = false
        mapIconBg.isSelected = false
        profileIconBg.isSelected = false

        // Set selected background
        when (selected) {
            "home" -> homeIconBg.isSelected = true
            "search" -> searchIconBg.isSelected = true
            "map" -> mapIconBg.isSelected = true
            "profile" -> profileIconBg.isSelected = true
        }

        // Set all icons and labels to primary color (no grey)
        val primaryColor = getColor(R.color.primary)

        iconHome.setColorFilter(primaryColor)
        labelHome.setTextColor(primaryColor)

        iconSearch.setColorFilter(primaryColor)
        labelSearch.setTextColor(primaryColor)

        labelMap.setTextColor(primaryColor)

        iconProfile.setColorFilter(primaryColor)
        labelProfile.setTextColor(primaryColor)
    }
}
