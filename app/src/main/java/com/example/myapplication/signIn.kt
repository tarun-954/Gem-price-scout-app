package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            val intent = Intent(this, Productdetailed::class.java)
            intent.putExtra("email_key", email)
            startActivity(intent)
        }
    }
}
