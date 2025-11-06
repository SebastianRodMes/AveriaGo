package com.sebastianrodriguez.averiago.auth

import Controller.UserController
import Entity.User
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sebastianrodriguez.averiago.R
import java.time.LocalDate
import Util

class SignUpActivity : AppCompatActivity() {
    lateinit var txtFullname: TextInputEditText
    lateinit var txtEmail: TextInputEditText
    lateinit var txtPhoneNumber: TextInputEditText
    lateinit var txtAddress: TextInputEditText
    lateinit var txtPass: TextInputEditText
    lateinit var txtConfirmPass: TextInputEditText
    lateinit var userController: UserController
    private var isEditMode : Boolean = false
    private lateinit var menuItemDelete: MenuItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
     userController  = UserController(this)
        txtFullname = findViewById<TextInputEditText>(R.id.etFullName)
        txtEmail = findViewById<TextInputEditText>(R.id.etEmail)
        txtPhoneNumber = findViewById<TextInputEditText>(R.id.etPhone)
        txtAddress = findViewById<TextInputEditText>(R.id.etAddress)
        txtPass = findViewById<TextInputEditText>(R.id.etPassword)
        txtConfirmPass = findViewById<TextInputEditText>(R.id.etConfirmPassword)

        val btnSearch = findViewById<ImageButton>(R.id.btnSearch_signup)
        btnSearch.setOnClickListener {
            searchPerson(txtEmail.text!!.trim().toString())
        }


    }//cierra el on create

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_crud, menu)
        menuItemDelete = menu!!.findItem(R.id.btn_delete_mnu)
        menuItemDelete.isVisible = isEditMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.btn_save_mnu->{
                if (isEditMode){
                    Util.showDialogCondition(this, getString(R.string.TextSaveActionQuestion), {savePerson()})
                }else {
                    savePerson()
                }
                return true
            }
            R.id.btn_delete_mnu -> {
                Util.showDialogCondition(this, getString(R.string.TextDeleteActionQuestion), {deletePerson()})
                return true
            }
            R.id.btn_cancel_mnu ->{
                cleanScreen()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    private fun searchPerson(email: String){
        try {
            val user = userController.getUserByEmail(email)
            if (user != null){
                isEditMode=true
                txtEmail.setText(user!!.email.toString())
                txtEmail.isEnabled=false
                txtFullname.setText(user.fullName.toString())
                txtPhoneNumber.setText(user.phoneNumber.toString())
                txtAddress.setText(user.address.toString())
                txtPass.setText(user.password.toString())
                menuItemDelete.isVisible = true
            }else{
                Toast.makeText(this, getString(R.string.MsgDataNotFound),
                    Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            cleanScreen()
            Toast.makeText(this, e.message.toString(),
                Toast.LENGTH_LONG).show()
        }
    }
    private fun cleanScreen(){

        isEditMode=false
        txtEmail.isEnabled = true
        txtFullname.setText("")
        txtEmail.setText("")
        txtPhoneNumber.setText("")
        txtAddress.setText("")
        txtPass.setText("")
        txtConfirmPass.setText("")

        invalidateOptionsMenu() //CORRE EL OnCreateOptionMenu
    }


    fun validationData(): Boolean {
        return txtEmail.text!!.trim().isNotEmpty() && txtFullname.text!!.trim().isNotEmpty()
                && txtPhoneNumber.text!!.trim().isNotEmpty() && txtAddress.text!!.trim().isNotEmpty()
                && txtPass.text!!.trim().isNotEmpty() && txtConfirmPass.text!!.trim().isNotEmpty()

    }

    fun savePerson() {
        try {
            if (validationData()) {
                if (txtPass.text.toString() != txtConfirmPass.text.toString()){
                    Toast.makeText(this, getString(R.string.MismatchPass), Toast.LENGTH_LONG).show()
                    return
                }
                if (userController.getUserByEmail(txtEmail.text.toString().trim()) != null
                    && !isEditMode) {
                    Toast.makeText(
                        this,
                        getString(R.string.MsgDuplicatedData),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val user = User()
                    user.fullName = txtFullname.text.toString()
                    user.email = txtEmail.text.toString()
                    user.phoneNumber = txtPhoneNumber.text.toString()
                    user.address = txtAddress.text.toString()
                    user.password= txtAddress.text.toString()





                    if (!isEditMode)
                        userController.addUser(user)



                    else
                        userController.updateUser(user)

                    cleanScreen()

                    Toast.makeText(
                        this, getString(R.string.MsgSaveSuccess), Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this, "Datos incompletos", Toast.LENGTH_LONG
                ).show()

            }


        } catch (e: Exception) {
            Toast.makeText(
                this, e.message.toString(), Toast.LENGTH_LONG
            ).show()
        }
    }

    fun deletePerson(): Unit {
        try {
            userController.deleteUser(txtEmail.text!!.trim().toString())
            cleanScreen()
            Toast.makeText(this, getString(R.string.MsgDeleteSucess), Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }


}