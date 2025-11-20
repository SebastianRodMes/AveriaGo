package com.sebastianrodriguez.averiago.auth

import Controller.UserController
import Entity.User
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.sebastianrodriguez.averiago.R
import com.sebastianrodriguez.averiago.utils.LoadingDialog
import Util

class SignUpActivity : AppCompatActivity() {
    private lateinit var txtFullname: TextInputEditText
    private lateinit var txtEmail: TextInputEditText
    private lateinit var txtPhoneNumber: TextInputEditText
    private lateinit var txtAddress: TextInputEditText
    private lateinit var txtPass: TextInputEditText
    private lateinit var txtConfirmPass: TextInputEditText

    private lateinit var userController: UserController
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadingDialog = LoadingDialog(this)
        userController = UserController(this) // usa SharedPrefsUserDataManager por defecto

        txtFullname = findViewById(R.id.etFullName)
        txtEmail = findViewById(R.id.etEmail)
        txtPhoneNumber = findViewById(R.id.etPhone)
        txtAddress = findViewById(R.id.etAddress)
        txtPass = findViewById(R.id.etPassword)
        txtConfirmPass = findViewById(R.id.etConfirmPassword)

        // Si ya hay al menos un usuario registrado, redirige al login
        if (userAlreadyRegistered()) {
            Toast.makeText(this, "Ya tienes una cuenta. Inicia sesión.", Toast.LENGTH_LONG).show()
            Util.openActivity(this, LogInActivity::class.java)
            finish()
            return
        }

        val btnCreateAccount = findViewById<MaterialButton>(R.id.btnCreateAccount)
        btnCreateAccount.setOnClickListener {
            if (validationData()) {
                savePerson()
            } else {
                Toast.makeText(this, "Datos incompletos o inválidos", Toast.LENGTH_LONG).show()
            }
        }

        val btnReturn = findViewById<ImageButton>(R.id.btnReturn)
        btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun validationData(): Boolean {
        val emailText = txtEmail.text?.toString()?.trim() ?: ""
        val fullNameText = txtFullname.text?.toString()?.trim() ?: ""
        val phoneText = txtPhoneNumber.text?.toString()?.trim() ?: ""
        val addressText = txtAddress.text?.toString()?.trim() ?: ""
        val passText = txtPass.text?.toString() ?: ""
        val confirmText = txtConfirmPass.text?.toString() ?: ""

        if (emailText.isEmpty() || fullNameText.isEmpty() || phoneText.isEmpty() ||
            addressText.isEmpty() || passText.isEmpty() || confirmText.isEmpty()) {
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) return false
        if (passText != confirmText) return false
        if (passText.length < 6) return false

        return true
    }

    private fun savePerson() {
        try {
            val email = txtEmail.text.toString().trim()

            val existing = try {
                userController.getUserByEmail(email)
            } catch (e: Exception) {
                null
            }

            if (existing != null) {
                Toast.makeText(this, "Ya existe un usuario con ese correo", Toast.LENGTH_LONG).show()
                return
            }

            val user = User().apply {
                fullName = txtFullname.text.toString().trim()
                this.email = email
                phoneNumber = txtPhoneNumber.text.toString().trim()
                address = txtAddress.text.toString().trim()
                password = txtPass.text.toString()
            }

            userController.addUser(user)

            // No guardamos password en SharedPreferences, persistencia ya está en SharedPrefsUserDataManager.
            loadingDialog.show("Creando cuenta...", 2000) {
                Toast.makeText(this, getString(R.string.MsgSaveSuccess), Toast.LENGTH_SHORT).show()
                Util.openActivity(this, LogInActivity::class.java)
                finish()
            }

        } catch (e: Exception) {
            loadingDialog.hide()
            Toast.makeText(this, e.message ?: "Error al crear usuario", Toast.LENGTH_LONG).show()
        }
    }

    private fun userAlreadyRegistered(): Boolean {
        val users = try {
            userController.getAllUsers()
        } catch (e: Exception) {
            null
        }
        return users != null && users.isNotEmpty()
    }
}