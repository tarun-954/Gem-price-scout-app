package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.gallery.GalleryFragment
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.slideshow.SlideshowFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNavView: BottomNavigationView =findViewById(R.id.bottom_navigation);
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_fav, R.id.nav_trends, R.id.nav_cart), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomNavView.setupWithNavController(navController)

        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_fav -> {
                    showHomeFragment()
                    true
                }
                R.id.nav_cart-> {
                    showSavedListFragment()
                    true
                }
                R.id.nav_trends -> {
                    showChatsListFragment()
                    true
                }
               
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showHomeFragment() {

        val homeFragment = GalleryFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, homeFragment)
            .commit()
    }

    private fun showChatsListFragment() {
      
        val chatsListFragment =HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, chatsListFragment)
            .commit()
    }

    private fun showSavedListFragment() {
       
        val savedListFragment = SlideshowFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, savedListFragment)
            .commit()
    }

  
}
