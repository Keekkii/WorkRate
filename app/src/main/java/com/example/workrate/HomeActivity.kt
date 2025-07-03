package com.example.workrate

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.workrate.HomeFragment
import com.example.workrate.SearchFragment
import com.example.workrate.MapFragment
import com.example.workrate.ProfileFragment


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

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize views
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // If you want drawer functionality (hamburger icon), wrap your root layout with DrawerLayout
        // For now, let's assume you added a DrawerLayout and set it here.
        drawerLayout = findViewById(R.id.drawer_layout) // you need to add this in XML!

        drawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle!!)
        drawerToggle!!.syncState()

        // Initialize bottom nav views as before
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
        showHamburger(false)

        setupBottomNav()
        setupAddButton()
    }

    private fun setupBottomNav() {
        navHome.setOnClickListener {
            loadFragment(HomeFragment())
            updateSelectedNav("home")
            showHamburger(false)
        }
        navSearch.setOnClickListener {
            loadFragment(SearchFragment())
            updateSelectedNav("search")
            showHamburger(false)
        }
        navMap.setOnClickListener {
            loadFragment(MapFragment())
            updateSelectedNav("map")
            showHamburger(false)
        }
        navProfile.setOnClickListener {
            loadFragment(ProfileFragment())
            updateSelectedNav("profile")
            showHamburger(true) // Show hamburger only here
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

        labelHome.setTextColor(resources.getColor(R.color.primary))
        labelSearch.setTextColor(resources.getColor(R.color.primary))
        labelMap.setTextColor(resources.getColor(R.color.primary))
        labelProfile.setTextColor(resources.getColor(R.color.primary))

        // Highlight selected
        when (selected) {
            "home" -> {
                homeIconBg.isSelected = true
                labelHome.setTextColor(resources.getColor(R.color.primary))
            }
            "search" -> {
                searchIconBg.isSelected = true
                labelSearch.setTextColor(resources.getColor(R.color.primary))
            }
            "map" -> {
                mapIconBg.isSelected = true
                labelMap.setTextColor(resources.getColor(R.color.primary))
            }
            "profile" -> {
                profileIconBg.isSelected = true
                labelProfile.setTextColor(resources.getColor(R.color.primary))
            }
        }
    }

    // Controls showing/hiding hamburger icon
    private fun showHamburger(show: Boolean) {
        if (show) {
            drawerToggle?.isDrawerIndicatorEnabled = true
            drawerToggle?.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            drawerToggle?.isDrawerIndicatorEnabled = false
            drawerToggle?.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
