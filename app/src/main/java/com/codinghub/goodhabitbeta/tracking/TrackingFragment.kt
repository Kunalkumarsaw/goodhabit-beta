package com.codinghub.goodhabitbeta.tracking

import android.app.Activity
import kotlinx.android.synthetic.main.tracking_fragment.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.codinghub.goodhabitbeta.databinding.TrackingFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*



class TrackingFragment : Fragment() {
    val TAG = "StepCounter"
    lateinit var binding: TrackingFragmentBinding
    private var totalSteps: String = "0"
    private val MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 101
    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 10

    enum class FitActionRequestCode {
        SUBSCRIBE,
        READ_DATA
    }

    private val fitnessOptions: FitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .build()
    private val account = context?.let { GoogleSignIn.getAccountForExtension(it, fitnessOptions) }


    companion object {
        fun newInstance() = TrackingFragment()
    }

    val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q


    private lateinit var viewModel: TrackingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        if (context?.let { ContextCompat.checkSelfPermission(
//                it,
//                android.Manifest.permission.ACTIVITY_RECOGNITION
//            ) }
//            != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//            requestPermissions(
//                arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
//                MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION
//            )
//        }else{
//            Log.d("debug", "Permissions granted !")
//        }
        binding = TrackingFragmentBinding.inflate(layoutInflater)
        // checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)

        return binding.root

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TrackingViewModel::class.java)
        // TODO: Use the ViewModel
        val signInAccount = GoogleSignIn.getLastSignedInAccount(context)
        if (!GoogleSignIn.hasPermissions(signInAccount, fitnessOptions)) {
            Log.d(TAG, "Gone for permissions ")
            GoogleSignIn.requestPermissions(
                this, // your activity
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g. 1
                signInAccount,
                fitnessOptions
            )
        } else {
            Log.d(TAG, "Gone for getting data ")
            GlobalScope.launch(Dispatchers.IO){
                subscribe()
                readData()
            }
            steps.text= "Total Steps = "+ totalSteps
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE -> readData()
                else -> {
                    // Result wasn't from Google Fit
                }
            }
            else -> {
                // Permission not granted
                Log.d("debugTracking", "Permission for fit not granted.")
            }
        }
    }

    /** Records step data by requesting a subscription to background step data.  */
    private fun subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        Fitness.getRecordingClient(requireContext(), getGoogleAccount())
            .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Successfully subscribed!")
                } else {
                    Log.w(TAG, "There was a problem subscribing.", task.exception)
                }
            }
    }

    private fun getGoogleAccount() = GoogleSignIn.getLastSignedInAccount(context)

    private fun accessGoogleFit() {
        val c = Calendar.getInstance()
        val startSeconds = c.timeInMillis
        c.set(Calendar.DAY_OF_WEEK, c.get(Calendar.DAY_OF_WEEK - 1))
        val endSeconds = c.timeInMillis
        //  val start = end.minusYears(1)
        // val endSeconds = end.atZone(ZoneId.systemDefault()).toEpochSecond()
        //  val startSeconds = start.atZone(ZoneId.systemDefault()).toEpochSecond()

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startSeconds, endSeconds, java.util.concurrent.TimeUnit.SECONDS)
            .bucketByTime(1, java.util.concurrent.TimeUnit.DAYS)
            .build()
        val account = GoogleSignIn.getLastSignedInAccount(context)
        Fitness.getHistoryClient(requireContext(), account)
            .readData(readRequest)
            .addOnSuccessListener {
                // Use response data here
                Log.i(TAG, "OnSuccess()")
            }
            .addOnFailureListener { e -> Log.d(TAG, "OnFailure()", e) }
    }

    private fun readData() {
        Fitness.getHistoryClient(context, getGoogleAccount())
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
                val total = when {
                    dataSet.isEmpty -> 0
                    else -> dataSet.dataPoints.first().getValue(Field.FIELD_STEPS).asInt()
                }
                Log.i(TAG, "Total steps: $total")
                totalSteps = total.toString()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was a problem getting the step count.", e)
            }
    }

}
