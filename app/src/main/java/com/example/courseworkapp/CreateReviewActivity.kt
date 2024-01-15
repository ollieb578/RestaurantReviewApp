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
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*

class CreateReviewActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_review)

        val mToolbar = findViewById<Toolbar>(R.id.create_review_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //gets email value from Firebase auth
        auth = Firebase.auth
        val email = auth.currentUser?.email
        //gets database instance
        database = FirebaseDatabase.getInstance("https://courseworkreviewapp-default-rtdb.europe-west1.firebasedatabase.app").reference

        //sets text at top of screen to user email
        val email_text = findViewById<TextView>(R.id.textEmailAddress)
        email_text.text = "Posting as $email"

        //collecting values from form
        val uid = auth.currentUser?.uid
        val rid = intent.getStringExtra("rid")
        val rname = intent.getStringExtra("rname")
        val rlocation = intent.getStringExtra("location")
        val score = findViewById<EditText>(R.id.create_review_score)
        val img = "null"    //TODO: add functionality
        val points = 1 //TODO: add functionality

        // gets location data
        var location : Location?= null
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        if (isLocationEnabled()) {
            if (checkPermission()) {
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
        }

        val reviewSubmit = findViewById<Button>(R.id.submit_button_review)

        reviewSubmit.setOnClickListener { view ->
            val uuid: UUID = UUID.randomUUID()
            val reviewid: String = uuid.toString()
            val reviewText = findViewById<EditText>(R.id.create_review_text)

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

            //review object created
            val review = Review(rid, rname,
                uid, reviewText.text.toString(),
                img, cityName,
                score.text.toString().toInt(), points,
                email)

            //request sent to server
            database.child("reviews").child(reviewid).setValue(review)
                .addOnCompleteListener(this)
                { task ->
                    if (task.isSuccessful) {
                        val newIntent = Intent(this, RestaurantActivity::class.java)
                        newIntent.putExtra("rid", rid)
                        newIntent.putExtra("rname", rname)
                        newIntent.putExtra("location", rlocation)

                        startActivity(newIntent)
                        finish()
                    } else {
                        closeKeyboard()
                        Snackbar.make(view, "Failed to create review", Snackbar.LENGTH_LONG).show()
                    }

                }
        }

    }

    //required to pass intent extras back to the restaurant activity.
    //it's very hacky but I couldn't find another way to deal with it.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            val newIntent = Intent(this, RestaurantActivity::class.java)
            newIntent.putExtra("rid", intent.getStringExtra("rid"))
            newIntent.putExtra("rname", intent.getStringExtra("rname"))
            newIntent.putExtra("location", intent.getStringExtra("location"))
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(newIntent)
            true
        } else {
            super.onOptionsItemSelected(item)
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