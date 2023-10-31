package com.masar.masatapp1.UI

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.masar.masatapp1.R
import com.masar.masatapp1.server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val MY_REQUEST_CODE : Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        check_for_update_availability()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationMainActivity) as NavHostFragment
        navController = navHostFragment.navController
        getAllDataServer()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val currentDestination = navController.currentDestination?.id
        if (currentDestination == R.id.homePage_Student_Fragment ||
            currentDestination == R.id.firstPage_Fragment||
            currentDestination == R.id.homeCompanyDeiverFragment||
            currentDestination == R.id.homeDriverFragment   ) {
            finish()
        } else {
            super.onBackPressed()
       }
    }
    @SuppressLint("SuspiciousIndentation")
    fun getAllDataServer(){
        val sharedPref = getSharedPreferences("Customer", Context.MODE_PRIVATE)
        var token = sharedPref.getString("Token","").toString()
        var who = sharedPref.getInt("who",0)
        CoroutineScope(Dispatchers.IO).launch {
            if (who==1){
                server.student.getAllBook_Activty(token)
            }else if (who==2)
                    server.dtiver.getAllBook(token.trim())
            else if (who==3)
                    server.company.getAllDriver(token)
        }
    }

    private fun check_for_update_availability(){
        val appUpdateManager = AppUpdateManagerFactory.create(this)

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MY_REQUEST_CODE)

            }


        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("MY_APP", "Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }

    }
}
