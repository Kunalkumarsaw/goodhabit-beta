package com.codinghub.goodhabitbeta

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.codinghub.goodhabitbeta.update.updateFragment
import com.codinghub.goodhabitbeta.databinding.ActivityMainBinding
import com.codinghub.goodhabitbeta.setting.SettingsFragment
import com.codinghub.goodhabitbeta.tools.ToolsFragment
import com.codinghub.goodhabitbeta.tracking.TrackingFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.security.Permissions
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    val TAG = "StepCounter"
    private val PermissionsRequestCode = 120
    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 101
    private val fitnessOptions: FitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .build()
    private var selectedFragment: Fragment? = null

    var appPermissions = arrayListOf<String>(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        var viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        setContentView(binding.root)
        binding.navigationBottomMain.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerMain, ToolsFragment()).commit()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appPermissions.add(android.Manifest.permission.ACTIVITY_RECOGNITION)
        }
        if (checkAndRequestPermissions()) {
            Log.d("permissions"," All the permissions are granted!")
            initapp();
        }

    }


    private fun checkAndRequestPermissions(): Boolean {
        // Check which permissions are granted
        val listPermissionsNeeded: MutableList<String> = mutableListOf()
        appPermissions.forEach { perms ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    perms
                ) != PackageManager.PERMISSION_DENIED
            ) {
                listPermissionsNeeded.add(perms)
            }
        }
        if (!listPermissionsNeeded.isNullOrEmpty()) {
                ActivityCompat.requestPermissions(
                    this,
                    listPermissionsNeeded.toTypedArray(), PermissionsRequestCode
                )
            return false
        }

        return true
    }

    private fun initapp() {
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            Log.d(TAG, "Gone for permissions ")
//            GoogleSignIn.requestPermissions(
//                this, // your activity
//                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g. 1
//                account,
//                fitnessOptions
//            )
        } else {
            Log.d(TAG, "Gone for getting data ")

            accessGoogleFit()
        }
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener() { item ->
        when (item.itemId) {
            R.id.bottom_nav_tools_main -> {
                selectedFragment = ToolsFragment()
            }
            R.id.bottom_nav_tracking_main -> {
                selectedFragment = TrackingFragment()
            }
            R.id.bottom_nav_update_main -> {
                selectedFragment = updateFragment()
            }
            R.id.bottom_nav_setting_main -> {
                selectedFragment = SettingsFragment()
            }
        }
        if (selectedFragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerMain, selectedFragment!!).commit()
        }
        return@OnNavigationItemSelectedListener true
    }

    override fun onBackPressed() {
        if (binding.navigationBottomMain.selectedItemId != R.id.bottom_nav_tools_main) {
            binding.navigationBottomMain.selectedItemId = R.id.bottom_nav_tools_main
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        selectedFragment?.onActivityResult(requestCode,resultCode,data)
        Log.d(TAG, "Gone for permissions On Activity Result ")
    }

    private fun accessGoogleFit() {

        Log.d(TAG, "You can get data")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionsRequestCode) {
            lateinit var permissionsResults: HashMap<String, Int>
            var deniedCount = 0
            // Gather permissions grant results
            grantResults.forEachIndexed { index, i ->
                if (i == PackageManager.PERMISSION_DENIED) {
                    permissionsResults.put(permissions[index], i)
                    deniedCount++
                }
            }
            // check if all permissions are granted
            if (deniedCount == 0)
                initapp()
            else {
                for (entry in permissionsResults.entries) {
                    val permName = entry.key
                    var permResult = entry.value
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        // show dialog of explanation
                        showDialog(
                            "",
                            "This app needs Location and Regocsition to work without and problems.",
                            "Yes, Grant permissions",
                            { dialog, which ->
                                dialog.dismiss()
                                checkAndRequestPermissions()
                            },
                            "No Exit app",
                            { dialog, which ->
                                dialog.dismiss()
                                finish()
                            },
                            false
                        )
                    }
                    //permission is denied (and never ask again is checked )
                    // should Show Request Permission Rationale will return false
                    else {
                        // ask user to go to settings and manually allow permissions
                        showDialog(
                            "",
                            "You have denied some permissions. Allow all permissions at [Settings] > [Permissions]",
                            "Go to Settings",
                            { dialog, which ->
                                dialog.dismiss()
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", packageName, null)
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            },
                            "No Exit app",
                            { dialog, which ->
                                dialog.dismiss()
                                finish()
                            },
                            false
                        )

                    }
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showDialog(
        title: String,
        msg: String,
        positiveLabel: String,
        positiveOnClick: DialogInterface.OnClickListener,
        negativeLabel: String,
        negativeOnClick: DialogInterface.OnClickListener,
        isCancelAble: Boolean
    ): AlertDialog {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setCancelable(isCancelAble)
        builder.setMessage(msg)
        builder.setPositiveButton(positiveLabel, positiveOnClick)
        builder.setNegativeButton(negativeLabel, negativeOnClick)

        val alert = builder.create()
        alert.show()
        return alert
    }
}