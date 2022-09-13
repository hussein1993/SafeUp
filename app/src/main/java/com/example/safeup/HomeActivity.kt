package com.example.safeup

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.safeup.adapters.VpAdapter
import com.example.safeup.models.HolidayModelItem
import com.example.safeup.models.HolidaysData
import com.example.safeup.viewmodels.FavoriteHolidaysViewModel
import com.example.safeup.viewmodels.HolidaysViewModel
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeActivity : AppCompatActivity() {
   // lateinit var restaurants : Restaurants
    lateinit var tab_layout : TabLayout
    lateinit var viewPagerAdapter : VpAdapter
    lateinit var viewPager : ViewPager2
    lateinit var location_lbl  : TextView
    private var CountryCode : String ="AT"

    private var mViewModel: HolidaysViewModel? = null
    private var favViewModel: FavoriteHolidaysViewModel? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var geocoder: Geocoder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mViewModel =  androidx.lifecycle.ViewModelProvider(this).get(HolidaysViewModel::class.java)
        favViewModel =  androidx.lifecycle.ViewModelProvider(this).get(FavoriteHolidaysViewModel::class.java)

        geocoder = Geocoder(this)

        favViewModel?.favHolidays?.observe(this){
            saveData()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()
        loadData()
        getUserCountry()
        //fetchData()
        linkUi()


    }


    private fun linkUi() {
        tab_layout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        viewPagerAdapter = VpAdapter(this)
        viewPager.adapter = viewPagerAdapter
        location_lbl= findViewById(R.id.location_lbl)

        TabLayoutMediator(tab_layout,viewPager){tab,position ->
            if (position == 0){
                tab.setIcon(R.drawable.ic_home)
            }else{
                tab.setIcon(R.drawable.ic_favorite)
            }

        }.attach()

        val defTab =  tab_layout.getTabAt(0)
        val tabIconColor = ContextCompat.getColor(applicationContext, R.color.red)
        defTab?.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        defTab?.select()
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.red)
                tab.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.gray)
                tab.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.red)
                tab.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }
        })

    }

    private val locationRequest: LocationRequest = LocationRequest.create()
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()

                fetchData(geocoder?.getFromLocation(location.latitude, location.longitude, 10)?.get(0)?.countryCode ?: "")
                updateLabels(geocoder?.getFromLocation(location.latitude, location.longitude, 10)?.get(0)?.countryCode)
            }
        }
    }

    private fun updateLabels(countryCode: String?) {

        location_lbl.text = countryCode

    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }


    fun getUserCountry(): String? {



        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {

            fetchData(geocoder?.getFromLocation(it.latitude, it.longitude, 10)?.get(0)?.countryCode ?: "")
            updateLabels(geocoder?.getFromLocation(it.latitude, it.longitude, 10)?.get(0)?.countryCode)
            // Toast.makeText(callingActivity,selftgeocoder?.getFromLocation(it.latitude,it.longitude,10)?.get(0)?.countryCode,Toast.LENGTH_SHORT).show()
        }

        return null
    }

    private fun fetchData(location: String) {
       var year = Calendar.getInstance().get(Calendar.YEAR).toString();
        CountryCode = location

        var apiService = RestClient.getClient(year,CountryCode).create(HolidaysApiService::class.java)
        val call: Call<HolidaysData> = apiService.fetchHolidays()
        call.enqueue(object : Callback<HolidaysData?> {
            override fun onResponse(call: Call<HolidaysData?>?, response: Response<HolidaysData?>) {

                if(response.body() == null){
                        Toast.makeText(this@HomeActivity,getString(R.string.error_location),Toast.LENGTH_LONG).show()
                }else{
                    HolidaysRepo.getInstance().allHolidays.value = response.body()?.getAllHolidaysList()
                }
            }

            override fun onFailure(call: Call<HolidaysData?>?, t: Throwable) {
                Log.e("error", "Got error : " + t.localizedMessage)
            }
        })
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(HolidaysRepo.getInstance().favHolidays.value)
        editor.putString("fav_holidays", json)
        editor.apply()
    }

    private fun loadData() {

        val sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE)

        val gson = Gson()
        val json = sharedPreferences.getString("fav_holidays", null)

        val _type = object : TypeToken<List<HolidayModelItem>>(){}.type

       val x =  (gson.fromJson<Any>(json,_type) as? java.util.ArrayList<HolidayModelItem>).let {
           if(it !=null) {
               HolidaysRepo.getInstance().favHolidays.value = it
           }
       }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        fusedLocationClient?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )

                        checkBackgroundLocation()
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationClient?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )

                        Toast.makeText(
                            this,
                            "Granted Background Location Permission",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return

            }
        }
    }


    private fun checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestBackgroundLocationPermission()
        }
    }
    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }
    companion object{

        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66

    }
}