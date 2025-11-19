package com.sebastianrodriguez.averiago.welcome

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sebastianrodriguez.averiago.R
import com.sebastianrodriguez.averiago.auth.SignUpActivity
import Util
import com.sebastianrodriguez.averiago.MainActivity
import com.sebastianrodriguez.averiago.auth.LogInActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnStart = findViewById<Button>(R.id.btnStart)
        btnStart.setOnClickListener {
            checkUserStatus()
        }
    }

    private fun checkUserStatus() {
        val sharedPref = getSharedPreferences("AveriaguApp", MODE_PRIVATE)
        val savedEmail = sharedPref.getString("email", null)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        when {
            // Usuario ya logueado va directo al MainActivity
            isLoggedIn -> {
                Util.openActivity(this, MainActivity::class.java)
                finish()
            }
            // Usuario registrado pero no logueado va a Login
            savedEmail != null -> {
                Util.openActivity(this, LogInActivity::class.java)
                finish()
            }
            // Usuario nuevo va a SignUp
            else -> {
                Util.openActivity(this, SignUpActivity::class.java)
                finish()
            }
        }
    }
}