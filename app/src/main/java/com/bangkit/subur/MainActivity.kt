package com.bangkit.subur

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bangkit.subur.databinding.ActivityMainBinding
import com.bangkit.subur.features.article.view.ArticleFragment
import com.bangkit.subur.features.community.view.CommunityFragment
import com.bangkit.subur.features.homepage.view.HomepageFragment
import com.bangkit.subur.features.profile.view.ProfileFragment
import com.bangkit.subur.features.riceplantdetector.view.RicePlantDetectorFragment

class MainActivity : AppCompatActivity(){

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_home -> openFragment(HomepageFragment())
                R.id.navigation_article -> openFragment(ArticleFragment())
                R.id.navigation_riceplantdetector -> openFragment(RicePlantDetectorFragment())
                R.id.navigation_community -> openFragment(CommunityFragment())
                R.id.navigation_profile -> openFragment(ProfileFragment())
            }
            true
        }
        fragmentManager = supportFragmentManager
        openFragment(HomepageFragment())
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}