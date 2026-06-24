package com.example.cuims

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * LoginActivity handles the Login process.
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Enable full-screen (edge-to-edge) design
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        
        // 2. Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 3. Find the Login button and set a click listener
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            // When clicked, navigate to the Home screen (MainActivity with ViewPager2)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            
            // Finish this activity so the user can't go back to Login via the back button
            finish()
        }
    }
}