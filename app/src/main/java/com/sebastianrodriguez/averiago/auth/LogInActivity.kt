package com.sebastianrodriguez.averiago.auth

import Entity.User
import Util
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.sebastianrodriguez.averiago.MainActivity
import com.sebastianrodriguez.averiago.R
import com.sebastianrodriguez.averiago.ViewModels.UserViewModel
import com.sebastianrodriguez.averiago.auth.SignUpActivity
import com.sebastianrodriguez.averiago.utils.LoadingDialog
import com.sebastianrodriguez.averiago.welcome.StartActivity
import Controller.UserController

class LogInActivity : AppCompatActivity() {
    lateinit var txtEmail: TextInputEditText
    lateinit var txtPass: TextInputEditText
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var userController: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadingDialog = LoadingDialog(this)
        userController = UserController(this)

        txtEmail = findViewById(R.id.etEmail_login)
        txtPass = findViewById(R.id.etPassword_login)

        // Precargar primer email registrado (si existe)
        loadSavedEmail()

        val btnReturn = findViewById<ImageButton>(R.id.btnReturn_login)
        btnReturn.setOnClickListener {
            finish()
        }

        val btnLogInActivity = findViewById<TextView>(R.id.btnLogin)
        btnLogInActivity.setOnClickListener {
            login()
        }
    }

    private fun loadSavedEmail() {
        val users = try {
            userController.getAllUsers()
        } catch (e: Exception) {
            null
        }
        val firstEmail = users?.firstOrNull()?.email
        if (firstEmail != null) {
            txtEmail.setText(firstEmail)
            txtPass.requestFocus()
        }
    }

    private fun login() {
        val emailInput = txtEmail.text.toString().trim()
        val passInput = txtPass.text.toString().trim()

        if (emailInput.isEmpty() || passInput.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val user = try {
            userController.getUserByEmail(emailInput)
        } catch (e: Exception) {
            null
        }

        if (user == null) {
            Toast.makeText(this, "No hay usuario registrado con ese correo", Toast.LENGTH_LONG).show()
            Util.openActivity(this, SignUpActivity::class.java)
            finish()
            return
        }

        if (user.password == passInput) {
            // Login exitoso, guardamos solo currentUserId / isLoggedIn en prefs (sin password)
            val sharedPref = getSharedPreferences("AveriaguApp_session", MODE_PRIVATE)
            sharedPref.edit().apply {
                putBoolean("isLoggedIn", true)
                putString("currentUserId", user.userId)
                apply()
            }

            loadingDialog.show("Iniciando Sesión...", 2000) {
                Toast.makeText(this, "Bienvenido!", Toast.LENGTH_SHORT).show()
                Util.openActivity(this, MainActivity::class.java)
                finish()
            }
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_LONG).show()
        }
    }
}