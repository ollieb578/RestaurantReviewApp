package com.example.courseworkapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class SearchActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var restaurantArrayList: ArrayList<Restaurant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //sets up app bar, allows up navigation
        val mToolbar = findViewById<Toolbar>(R.id.search_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //get uid from Firebase auth
        auth = Firebase.auth
        val uid = auth.currentUser?.uid

        //no button to create restaurants if user not logged in
        if (uid == null) {
            findViewById<FloatingActionButton>(R.id.createRestaurantFab).visibility = View.GONE
        }

        //setting up RecyclerView
        userRecyclerView = findViewById(R.id.restaurantRecycler)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        restaurantArrayList = arrayListOf<Restaurant>()
        getRestaurantData()

    }

    private fun getRestaurantData() {
        dbref = FirebaseDatabase.getInstance("https://courseworkreviewapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("restaurants")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (restaurantSnapshot in snapshot.children) {
                        val restaurant = restaurantSnapshot.getValue(Restaurant::class.java)

                        restaurantArrayList.add(restaurant!!)
                    }
                    userRecyclerView.adapter = RestaurantAdapter(restaurantArrayList) {position -> onListItemClick(position)}

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    fun onListItemClick (position : Int) {
        val intent = Intent(this, RestaurantActivity::class.java)
        intent.putExtra("rid", restaurantArrayList[position].rid)
        intent.putExtra("rname", restaurantArrayList[position].name)
        intent.putExtra("location", restaurantArrayList[position].location)
        startActivity(intent)
    }

    fun openCreateRestaurantActivity(view: View) {
        val intent = Intent(this, CreateRestaurantActivity::class.java)
        startActivity(intent)
    }

}