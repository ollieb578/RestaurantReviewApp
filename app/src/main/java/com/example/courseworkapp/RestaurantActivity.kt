package com.example.courseworkapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class RestaurantActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var reviewArrayList: ArrayList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        val mToolbar = findViewById<Toolbar>(R.id.restaurant_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //sets text at top of screen to restaurant name
        val rid = intent.getStringExtra("rid")
        val rname = intent.getStringExtra("rname")
        val rlocation = intent.getStringExtra("location")

        val restaurant_text = findViewById<TextView>(R.id.textRestaurantName)
        val location_text = findViewById<TextView>(R.id.locationPlaceholder)

        restaurant_text.text = "$rname"
        location_text.text = "$rlocation"

        //gets email value from Firebase auth
        auth = Firebase.auth
        val uid = auth.currentUser?.uid
        

        //no button to create reviews if user not logged in
        if (uid == null) {
            findViewById<FloatingActionButton>(R.id.createReviewFab).visibility = View.GONE
        }

        //setting up RecyclerView
        userRecyclerView = findViewById(R.id.restaurantReviewRecycler)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        reviewArrayList = arrayListOf<Review>()
        getRestaurantReviewData()
    }

    private fun getRestaurantReviewData() {
        dbref = FirebaseDatabase.getInstance("https://courseworkreviewapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("reviews")
        val rid = intent.getStringExtra("rid")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (reviewSnapshot in snapshot.children) {
                        val review = reviewSnapshot.getValue(Review::class.java)

                        if (review?.rid == rid) {
                            reviewArrayList.add(review!!)
                        }
                    }
                    userRecyclerView.adapter = RestaurantReviewAdapter(reviewArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    fun openCreateReviewActivity(view: View) {
        val newIntent = Intent(this, CreateReviewActivity::class.java)
        newIntent.putExtra("rid", intent.getStringExtra("rid"))
        newIntent.putExtra("rname", intent.getStringExtra("rname"))
        newIntent.putExtra("location", intent.getStringExtra("location"))
        startActivity(newIntent)
    }
}