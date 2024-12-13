package com.bangkit.subur

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.bangkit.subur.databinding.ActivityMainBinding
import com.bangkit.subur.features.article.view.ArticleFragment
import com.bangkit.subur.features.community.view.CommunityFragment
import com.bangkit.subur.features.homepage.view.HomepageFragment
import com.bangkit.subur.features.login.view.LoginActivity
import com.bangkit.subur.features.profile.view.ProfileFragment
import com.bangkit.subur.features.riceplantdetector.view.RicePlantDetectorFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.bangkit.subur.preferences.LocationPreferences
import com.bangkit.subur.preferences.UserPreferences
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPreference: LocationPreferences
    private lateinit var locationManager: LocationManager
    private lateinit var userPreferences: UserPreferences

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val LOCATION_SETTINGS_REQUEST_CODE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationPreference = LocationPreferences(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        userPreferences = UserPreferences(this)

        checkUserLogin()
    }

    private fun checkUserLogin() {
        lifecycleScope.launch {
            userPreferences.isLoggedIn().collect { isLoggedIn ->
                if (isLoggedIn) {
                    checkLocationPermission()

                    setupMainActivityUI()
                    checkLocationPermission()
                    checkLocationPermission()
                    setupMainActivityUI()

                    val lastPage = getLastSelectedPage()
                    when (lastPage) {
                        "home" -> openFragment(HomepageFragment())
                        "article" -> openFragment(ArticleFragment())
                        "riceplantdetector" -> openFragment(RicePlantDetectorFragment())
                        "community" -> openFragment(CommunityFragment())
                        "profile" -> openFragment(ProfileFragment())
                        else -> openFragment(HomepageFragment())
                    }


                } else {

                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun setupMainActivityUI() {
        binding.fab.setOnClickListener {
            openFragment(RicePlantDetectorFragment())
            binding.bottomNavigation.selectedItemId = R.id.navigation_riceplantdetector
        }

        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_home -> {
                    saveLastSelectedPage("home")
                    openFragment(HomepageFragment())
                }
                R.id.navigation_article -> {
                    saveLastSelectedPage("article")
                    openFragment(ArticleFragment())
                }
                R.id.navigation_riceplantdetector -> {
                    saveLastSelectedPage("riceplantdetector")
                    openFragment(RicePlantDetectorFragment())
                }
                R.id.navigation_community -> {
                    saveLastSelectedPage("community")
                    openFragment(CommunityFragment())
                }
                R.id.navigation_profile -> {
                    saveLastSelectedPage("profile")
                    openFragment(ProfileFragment())
                }
            }
            true
        }
        fragmentManager = supportFragmentManager
    }

    private fun saveLastSelectedPage(page: String) {
        val sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("lastSelectedPage", page)
        editor.apply()
    }

    private fun getLastSelectedPage(): String? {
        val sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("lastSelectedPage", "home")
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            AlertDialog.Builder(this)
                .setTitle("Location Permission Required")
                .setMessage("This app requires location access to provide accurate services. Please grant location permission.")
                .setPositiveButton("Grant Permission") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
                .setNegativeButton("Exit App") { _, _ -> finish() }
                .setCancelable(false)
                .show()
        } else {
            checkLocationServices()
        }
    }

    private fun checkLocationServices() {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled && !isNetworkEnabled) {

            AlertDialog.Builder(this)
                .setTitle("Location Services Disabled")
                .setMessage("Location services are necessary for this app to function. Please enable location services.")
                .setPositiveButton("Enable Location") { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, LOCATION_SETTINGS_REQUEST_CODE)
                }
                .setNegativeButton("Exit App") { _, _ -> finish() }
                .setCancelable(false)
                .show()
        } else {

            setupMainActivityUI()
            updateLocation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {

            checkLocationServices()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationServices()
            } else {

                AlertDialog.Builder(this)
                    .setTitle("Permission Denied")
                    .setMessage("Location permission is required for this app to function. The app will now close.")
                    .setPositiveButton("Exit") { _, _ ->
                        finish()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            checkLocationServices()
        }
    }

    private fun updateLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        lifecycleScope.launch {
                            locationPreference.saveLocation(it.latitude, it.longitude)
                        }
                    }
                }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Check if there are fragments in the back stack
            if (supportFragmentManager.backStackEntryCount > 1) {
                // If there are multiple fragments, pop the top fragment
                supportFragmentManager.popBackStack()
            } else {
                // If it's the last fragment, exit the app
                finish()
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}