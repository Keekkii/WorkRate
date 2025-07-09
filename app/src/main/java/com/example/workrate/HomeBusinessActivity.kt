package com.example.workrate

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

    private lateinit var toolbar: Toolbar

    private var currentTab = "home"  // Track selected tab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_business)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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
        showToolbar(false) // Hide toolbar initially (only show on profile)

        navHome.setOnClickListener {
            selectNavItem("home")
            loadFragment(HomeBusinessFragment())
            showToolbar(false)
        }
        navSearch.setOnClickListener {
            selectNavItem("search")
            loadFragment(PostAJobFragment())
            showToolbar(false)
        }
        navMap.setOnClickListener {
            selectNavItem("map")
            loadFragment(MapFragment())
            showToolbar(false)
        }
        navProfile.setOnClickListener {
            selectNavItem("profile")
            loadFragment(ProfileFragment())
            showToolbar(true)
        }
    }

    private fun selectNavItem(item: String) {
        currentTab = item

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

        invalidateOptionsMenu() // Refresh menu to show/hide hamburger
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showToolbar(show: Boolean) {
        toolbar.visibility = if (show) Toolbar.VISIBLE else Toolbar.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val menuItem = menu?.findItem(R.id.action_drawer_toggle)
        menuItem?.isVisible = (currentTab == "profile") // Show hamburger only on profile tab
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_drawer_toggle -> {
                val bottomSheet = SettingsBottomSheet()
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
