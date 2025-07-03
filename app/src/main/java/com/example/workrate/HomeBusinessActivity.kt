package com.example.workrate

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class HomeBusinessActivity : AppCompatActivity() {

    private lateinit var homeIconBg: FrameLayout
    private lateinit var searchIconBg: FrameLayout
    private lateinit var mapIconBg: FrameLayout
    private lateinit var profileIconBg: FrameLayout

    private lateinit var navHome: LinearLayout
    private lateinit var navSearch: LinearLayout
    private lateinit var navMap: LinearLayout
    private lateinit var navProfile: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_business)

        homeIconBg = findViewById(R.id.home_icon_bg)
        searchIconBg = findViewById(R.id.search_icon_bg)
        mapIconBg = findViewById(R.id.map_icon_bg)
        profileIconBg = findViewById(R.id.profile_icon_bg)

        navHome = findViewById(R.id.nav_home)
        navSearch = findViewById(R.id.nav_search)
        navMap = findViewById(R.id.nav_map)
        navProfile = findViewById(R.id.nav_profile)

        // Initially select home and load Home fragment
        selectNavItem("home")
        loadFragment(HomeBusinessFragment()) // Replace with your actual fragment class

        navHome.setOnClickListener {
            selectNavItem("home")
            loadFragment(HomeBusinessFragment())
        }
        navSearch.setOnClickListener {
            selectNavItem("search")
            loadFragment(PostAJobFragment())
        }
        navMap.setOnClickListener {
            selectNavItem("map")
            loadFragment(MapFragment())
        }
        navProfile.setOnClickListener {
            selectNavItem("profile")
            loadFragment(ProfileFragment())
        }
    }

    private fun selectNavItem(item: String) {
        homeIconBg.setBackgroundResource(android.R.color.transparent)
        searchIconBg.setBackgroundResource(android.R.color.transparent)
        mapIconBg.setBackgroundResource(android.R.color.transparent)
        profileIconBg.setBackgroundResource(android.R.color.transparent)

        when (item) {
            "home" -> homeIconBg.setBackgroundResource(R.drawable.ellipse_679)
            "search" -> searchIconBg.setBackgroundResource(R.drawable.ellipse_679)
            "map" -> mapIconBg.setBackgroundResource(R.drawable.ellipse_679)
            "profile" -> profileIconBg.setBackgroundResource(R.drawable.ellipse_679)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

