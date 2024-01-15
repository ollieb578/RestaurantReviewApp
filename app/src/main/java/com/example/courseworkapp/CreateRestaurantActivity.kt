package com.example.courseworkapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*

class CreateRestaurantActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_restaurant)

        val mToolbar = findViewById<Toolbar>(R.id.create_restaurant_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //gets uid value from Firebase auth
        auth = Firebase.auth
        //gets database instance
        database = FirebaseDatabase.getInstance("https://courseworkreviewapp-default-rtdb.europe-west1.firebasedatabase.app").reference

        //getting location data
        var location : Location ?= null

        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        if (isLocationEnabled()) {
            if (checkPermission()) {
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
        }

        val restaurantSubmit = findViewById<Button>(R.id.submit_button_restaurant)

        restaurantSubmit.setOnClickListener { view ->
            val uuid: UUID = UUID.randomUUID()
            val rid: String = uuid.toString()
            val name = findViewById<EditText>(R.id.create_restaurant_text)

            //Geocoder converts location data (latitude, longitude) into a city name.
            val cityName : String = if (location != null) {
                val gcd = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> =
                    location.let {
                        gcd.getFromLocation(it.latitude, location.longitude, 1) } as List<Address>
                addresses[0].locality
            } else {
                "Not available."
            }
            //restaurant object created
            val restaurant = Restaurant(rid, 0, cityName, name.text.toString())

            //request is sent to server
            database.child("restaurants").child(rid).setValue(restaurant)
                .addOnCompleteListener(this)
            { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, SearchActivity::class.java)

                    startActivity(intent)
                    finish()
                } else {
                    closeKeyboard()
                    Snackbar.make(view, "Failed to create restaurant", Snackbar.LENGTH_LONG).show()
                }

            }
        }
    }

    public override fun onStart() {
        super.onStart()

        //if user is not logged in, they are moved to MainActivity
        val currentUser = auth.currentUser

        if (currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    //Code for getting location data. I had no luck getting any of this working.
    // Tried to replicate what I saw from the lecture but found another way to get it working without FusedLocation.
//    private val mLocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            var mLastLocation: Location = locationResult.lastLocation
//
//        }
//    }
//
//    private fun getLocation() {
//        if (isLocationEnabled()) {
//            if (checkPermission()){
//                val mLocationRequest = LocationRequest()
//                mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//                mLocationRequest.interval = 0
//                mLocationRequest.fastestInterval = 0
//                mLocationRequest.numUpdates = 1
//
//                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//                mFusedLocationClient!!.requestLocationUpdates(
//                    mLocationRequest, mLocationCallback,
//                    Looper.myLooper()
//                )
//
//            }
//        }
//    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        } else {
            val PERMISSION_ID = 24
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID )
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}