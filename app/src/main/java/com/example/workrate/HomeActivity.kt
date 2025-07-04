package com.example.workrate

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.os.Build
import android.view.View


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.light_grey)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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

        loadFragment(HomeFragment())
        updateSelectedNav("home")
        showToolbar(false)

        setupBottomNav()
        setupAddButton()
    }

    private fun setupBottomNav() {
        navHome.setOnClickListener {
            loadFragment(HomeFragment())
            updateSelectedNav("home")
            showToolbar(false)
        }
        navSearch.setOnClickListener {
            loadFragment(SearchFragment())
            updateSelectedNav("search")
            showToolbar(false)
        }
        navMap.setOnClickListener {
            loadFragment(MapFragment())
            updateSelectedNav("map")
            showToolbar(false)
        }
        navProfile.setOnClickListener {
            loadFragment(ProfileFragment())
            updateSelectedNav("profile")
            showToolbar(true)
        }
    }

    private fun setupAddButton() {
        navAdd.setOnClickListener {
            Toast.makeText(this, "Add button clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun updateSelectedNav(selected: String) {
        homeIconBg.isSelected = false
        searchIconBg.isSelected = false
        mapIconBg.isSelected = false
        profileIconBg.isSelected = false

        labelHome.setTextColor(ContextCompat.getColor(this, R.color.primary))
        labelSearch.setTextColor(ContextCompat.getColor(this, R.color.primary))
        labelMap.setTextColor(ContextCompat.getColor(this, R.color.primary))
        labelProfile.setTextColor(ContextCompat.getColor(this, R.color.primary))

        when (selected) {
            "home" -> homeIconBg.isSelected = true
            "search" -> searchIconBg.isSelected = true
            "map" -> mapIconBg.isSelected = true
            "profile" -> profileIconBg.isSelected = true
        }
    }

    private fun showToolbar(show: Boolean) {
        toolbar.visibility = if (show) Toolbar.VISIBLE else Toolbar.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_drawer_toggle -> {
                // Replace with showing a BottomSheet
                val bottomSheet = SettingsBottomSheet()
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
