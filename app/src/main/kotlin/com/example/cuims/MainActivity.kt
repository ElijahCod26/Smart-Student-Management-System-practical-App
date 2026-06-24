package com.example.cuims

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 1. Initialize Views using findViewById
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        navView = findViewById(R.id.navView)

        // 2. Setup the Toolbar
        setSupportActionBar(toolbar)
        
        // 3. Setup Navigation Drawer
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.home,
            R.string.home
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            if (itemSelection(menuItem.itemId)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 4. Set up ViewPager2 with Adapter
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // 5. Connect TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.home)
                1 -> getString(R.string.profile)
                2 -> getString(R.string.settings)
                else -> null
            }
        }.attach()

        // 6. Dynamically update Toolbar Title and Nav Drawer selection
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val titleRes = when (position) {
                    0 -> R.string.dashboard
                    1 -> R.string.my_profile
                    2 -> R.string.app_settings
                    else -> R.string.app_name
                }
                supportActionBar?.title = getString(titleRes)
                
                // Sync Nav View selection
                val navItemId = when (position) {
                    0 -> R.id.nav_home
                    1 -> R.id.nav_profile
                    2 -> R.id.nav_settings
                    else -> -1
                }
                if (navItemId != -1) {
                    navView.setCheckedItem(navItemId)
                }
            }
        })

        // 7. Handle Back Press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun itemSelection(itemId: Int): Boolean {
        return when (itemId) {
            R.id.nav_home -> {
                viewPager.currentItem = 0
                true
            }
            R.id.nav_profile -> {
                viewPager.currentItem = 1
                true
            }
            R.id.nav_settings -> {
                viewPager.currentItem = 2
                true
            }
            R.id.nav_logout -> {
                showLogoutConfirmation()
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return when (item.itemId) {
            R.id.menu_about -> {
                Toast.makeText(this, "Student Registration System v1.0", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_help -> {
                Toast.makeText(this, "Contact support at support@example.com", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_logout -> {
                showLogoutConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.confirm_logout)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                performLogout()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun performLogout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}