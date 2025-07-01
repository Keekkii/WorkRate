package com.example.workrate

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private lateinit var navHome: LinearLayout
    private lateinit var navSearch: LinearLayout
    private lateinit var navMap: LinearLayout
    private lateinit var navProfile: LinearLayout
    private lateinit var navAdd: ImageView

    private lateinit var homeIconBg: FrameLayout
    private lateinit var searchIconBg: FrameLayout
    private lateinit var mapIconBg: FrameLayout
    private lateinit var profileIconBg: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Find views
        navHome = findViewById(R.id.nav_home)
        navSearch = findViewById(R.id.nav_search)
        navMap = findViewById(R.id.nav_map)
        navProfile = findViewById(R.id.nav_profile)
        navAdd = findViewById(R.id.nav_add)

        homeIconBg = findViewById(R.id.home_icon_bg)
        searchIconBg = findViewById(R.id.search_icon_bg)
        mapIconBg = findViewById(R.id.map_icon_bg)
        profileIconBg = findViewById(R.id.profile_icon_bg)

        // Show HomeFragment by default and highlight HOME
        selectTab(Tab.HOME)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()

        navHome.setOnClickListener {
            selectTab(Tab.HOME)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
        navSearch.setOnClickListener {
            selectTab(Tab.SEARCH)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .commit()
        }
        navAdd.setOnClickListener {
            clearTabHighlights()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddFragment())
                .commit()
        }
        navMap.setOnClickListener {
            selectTab(Tab.MAP)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MapFragment())
                .commit()
        }
        navProfile.setOnClickListener {
            selectTab(Tab.PROFILE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .commit()
        }
        val analytics = FirebaseAnalytics.getInstance(this)
        analytics.logEvent("test_event", null)
    }

    private fun clearTabHighlights() {
        homeIconBg.setBackgroundResource(android.R.color.transparent)
        searchIconBg.setBackgroundResource(android.R.color.transparent)
        mapIconBg.setBackgroundResource(android.R.color.transparent)
        profileIconBg.setBackgroundResource(android.R.color.transparent)
    }

    private fun selectTab(tab: Tab) {
        clearTabHighlights()
        when (tab) {
            Tab.HOME -> homeIconBg.setBackgroundResource(R.drawable.ellipse_679)
            Tab.SEARCH -> searchIconBg.setBackgroundResource(R.drawable.ellipse_679)
            Tab.MAP -> mapIconBg.setBackgroundResource(R.drawable.ellipse_679)
            Tab.PROFILE -> profileIconBg.setBackgroundResource(R.drawable.ellipse_679)
        }
    }

    enum class Tab { HOME, SEARCH, MAP, PROFILE }
}
