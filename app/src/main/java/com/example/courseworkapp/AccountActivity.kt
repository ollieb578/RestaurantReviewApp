package com.example.courseworkapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class AccountActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var reviewArrayList: ArrayList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        //sets up app bar, allows up navigation
        val mToolbar = findViewById<Toolbar>(R.id.account_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //gets email value from Firebase auth
        auth = Firebase.auth
        val email = auth.currentUser?.email

        //sets text at top of screen to user email
        val email_text = findViewById<TextView>(R.id.textEmailAddress)
        email_text.text = "$email"

        //setting up RecyclerView
        userRecyclerView = findViewById(R.id.accountReviewRecycler)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        reviewArrayList = arrayListOf<Review>()
        getAccountReviewData()
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        if (currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getAccountReviewData() {
        dbref = FirebaseDatabase.getInstance("https://courseworkreviewapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("reviews")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (reviewSnapshot in snapshot.children) {
                        val review = reviewSnapshot.getValue(Review::class.java)

                        val currentUser = auth.currentUser

                        if (review?.uid == currentUser?.uid) {
                            reviewArrayList.add(review!!)
                        }
                    }
                    userRecyclerView.adapter = AccountReviewAdapter(reviewArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

}