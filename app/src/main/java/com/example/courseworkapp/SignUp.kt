package com.example.courseworkapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //sets up app bar
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)

        //sends request to create account to Firebase auth
        auth = Firebase.auth

        val email = findViewById<EditText>(R.id.signUpEmailAddress)
        val password = findViewById<EditText>(R.id.signUpPassword)

        val loginSubmit = findViewById<Button>(R.id.submit_button_signup)
        loginSubmit.setOnClickListener { view ->
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this)
                { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)

                        startActivity(intent)
                        finish()
                    } else {
                        closeKeyboard()
                        Snackbar.make(view, "Failed to create user", Snackbar.LENGTH_LONG).show()
                    }

                }
        }
    }

    public override fun onStart() {
        super.onStart()

        //if user is logged in, they are moved to MainActivity
        val currentUser = auth.currentUser

        if (currentUser != null) {
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

}