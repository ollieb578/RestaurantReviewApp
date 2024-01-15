package com.example.courseworkapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)

        auth = Firebase.auth

        if (auth.currentUser != null) {
            findViewById<Button>(R.id.Login_button).visibility = View.INVISIBLE
            findViewById<Button>(R.id.Signup_button).visibility = View.INVISIBLE

            val email = auth.currentUser?.email
            val welcome_text = findViewById<TextView>(R.id.welcome_text)

            welcome_text.text = "Welcome $email!"

        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_layout, menu)
        auth = Firebase.auth

        if (auth.currentUser == null) {
            menu?.findItem(R.id.account_button)?.isVisible = false
            menu?.findItem(R.id.logout_button)?.isVisible = false
        }

        return super.onCreateOptionsMenu(menu)
    }

    fun openLogin(view: View) {
        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)
        finish()
    }

    fun openSignup(view: View) {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
        finish()
    }

    fun logout(item: MenuItem) {
        Firebase.auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun openAccount(item: MenuItem) {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    fun openSearch(item: MenuItem) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}