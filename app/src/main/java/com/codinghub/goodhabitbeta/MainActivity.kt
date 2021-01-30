package com.codinghub.goodhabitbeta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.codinghub.goodhabitbeta.databinding.ActivityMainBinding
import com.codinghub.goodhabitbeta.setting.SettingsFragment
import com.codinghub.goodhabitbeta.tools.ToolsFragment
import com.codinghub.goodhabitbeta.tracking.TrackingFragment
import com.codinghub.goodhabitbeta.update.updateFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        var viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        setContentView(binding.root)
        binding.navigationBottomMain.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerMain,ToolsFragment()).commit()


    }
    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener(){ item->
        var selectedFragment :Fragment? = null
        when(item.itemId){
            R.id.bottom_nav_tools_main -> {selectedFragment=ToolsFragment() }
            R.id.bottom_nav_tracking_main -> {selectedFragment=TrackingFragment()}
            R.id.bottom_nav_update_main-> {selectedFragment=updateFragment()}
            R.id.bottom_nav_setting_main-> {selectedFragment=SettingsFragment()}
        }
        if (selectedFragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerMain,selectedFragment).commit()
        }
        return@OnNavigationItemSelectedListener true
    }

    override fun onBackPressed() {
        if (binding.navigationBottomMain.selectedItemId!=R.id.bottom_nav_tools_main){
            binding.navigationBottomMain.selectedItemId = R.id.bottom_nav_tools_main
        }else{
            super.onBackPressed()
        }
    }
}