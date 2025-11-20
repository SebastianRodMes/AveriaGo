package com.sebastianrodriguez.averiago.report

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.sebastianrodriguez.averiago.MainActivity
import com.sebastianrodriguez.averiago.R
import com.sebastianrodriguez.averiago.utils.LoadingDialog
import Controller.TicketController
import Controller.UserController
import Entity.Ticket
import Entity.TimelineEvent
import java.util.UUID
import Util
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.card.MaterialCardView

class CreateReportStep3Activity : AppCompatActivity() {

    private lateinit var serviceType: String
    private lateinit var problemType: String
    private lateinit var problemDescription: String

    private lateinit var tvCategoryValue: TextView
    private lateinit var tvProblemValue: TextView
    private lateinit var etDescription: EditText
    private lateinit var tvLocationName: TextView

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var ticketController: TicketController
    private lateinit var userController: UserController
    private lateinit var imgPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_report_step3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadingDialog = LoadingDialog(this)
        ticketController = TicketController(this)
        userController = UserController(this)

        // Obtener datos del Step 2
        serviceType = intent.getStringExtra("SERVICE_TYPE") ?: ""
        problemType = intent.getStringExtra("PROBLEM_TYPE") ?: ""
        problemDescription = intent.getStringExtra("DESCRIPTION") ?: ""

        setupToolbar()
        setupViews()
        setupPhoto()
        loadSummaryData()
        loadUserLocation()
        setupSubmitButton()
    }

    private fun setupPhoto() {
        val cardPhotos = findViewById<MaterialCardView>(R.id.cardPhotos)
        cardPhotos.setOnClickListener {
            // Mostrar opciones para elegir entre cámara o galería
            val options = arrayOf("Tomar foto", "Seleccionar de galería")
            android.app.AlertDialog.Builder(this)
                .setTitle("Agregar foto")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> takePhoto()
                        1 -> selectPhoto()
                    }
                }
                .show()
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish() // Volver al Step 2
        }
    }

    private fun setupViews() {
        tvCategoryValue = findViewById(R.id.tvCategoryValue)
        tvProblemValue = findViewById(R.id.tvProblemValue)
        etDescription = findViewById(R.id.etDescription)
        tvLocationName = findViewById(R.id.tvLocationName)
        imgPhoto = findViewById(R.id.iconCamera)

        // Precargar la descripción del Step 2
        etDescription.setText(problemDescription)
    }

    private fun loadSummaryData() {
        tvCategoryValue.text = serviceType
        tvProblemValue.text = problemType

        findViewById<TextView>(R.id.tvCategoryLabel).text = "Categoría: $serviceType"
        findViewById<TextView>(R.id.tvProblemLabel).text = "Problema: $problemType"
    }

    private fun loadUserLocation() {
        // Cargar la ubicación desde SharedPreferences (perfil)
        val sharedPref = getSharedPreferences("AveriaguApp", MODE_PRIVATE)
        val address = sharedPref.getString("address", "No especificada")
        tvLocationName.text = address
    }

    private fun setupSubmitButton() {
        val btnSubmitReport = findViewById<MaterialButton>(R.id.btnSubmitReport)
        btnSubmitReport.setOnClickListener {
            if (validateInputs()) {
                submitReport()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val description = etDescription.text.toString().trim()

        if (description.isEmpty()) {
            Toast.makeText(this, "Por favor describe el problema", Toast.LENGTH_SHORT).show()
            etDescription.requestFocus()
            return false
        }

        if (description.length < 10) {
            Toast.makeText(this, "La descripción debe tener al menos 10 caracteres", Toast.LENGTH_SHORT).show()
            etDescription.requestFocus()
            return false
        }

        return true
    }

    private fun submitReport() {
        val finalDescription = etDescription.text.toString().trim()

        loadingDialog.show("Enviando reporte...", 2000) {
            try {
                // Obtener currentUserId de la sesión
                val sessionPref = getSharedPreferences("AveriaguApp_session", MODE_PRIVATE)
                val currentUserId = sessionPref.getString("currentUserId", null)

                // Si no hay sesión, intentar obtener el primer usuario registrado (fallback)
                val userId = currentUserId ?: run {
                    val users = userController.getAllUsers()
                    users?.firstOrNull()?.userId
                }

                if (userId == null) {
                    Toast.makeText(this, "No se encontró usuario. Regístrate primero.", Toast.LENGTH_LONG).show()
                    Util.openActivity(this, com.sebastianrodriguez.averiago.auth.SignUpActivity::class.java)
                    finish()
                    return@show
                }

                // Generar ticketId único (cambiar ya que genera un id muy extenso)
                val ticketId = UUID.randomUUID().toString()

                val sharedPref = getSharedPreferences("AveriaguApp", MODE_PRIVATE)
                val address = sharedPref.getString("address", "No especificada") ?: "No especificada"

                // Obtener la foto
                val photoBitmap = try {
                    (imgPhoto.drawable as? BitmapDrawable)?.bitmap
                } catch (e: Exception) {
                    null
                }

                val ticket = Ticket(
                    ticketId = ticketId,
                    userId = userId,
                    category = serviceType,
                    subcategory = problemType,
                    description = finalDescription,
                    location = address,
                    photo = photoBitmap
                )

                // Añadir un evento inicial al timeline
                val initialEvent = TimelineEvent(
                    actorId = userId,
                    message = "Reporte creado por el usuario"
                )
                ticket.addTimelineEvent(initialEvent)

                // Persistir ticket
                ticketController.addTicket(ticket)

                // Agregar el ticket al usuario y persistir user
                val user = userController.getUserById(userId)
                if (user != null) {
                    user.tickets.add(ticket)
                    userController.updateUser(user)
                }

                Toast.makeText(this, "¡Reporte enviado exitosamente!", Toast.LENGTH_LONG).show()

                // Volver al MainActivity y limpiar el stack
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Error al crear el reporte: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val cameraPreviewLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap != null) {
            // Configurar la imagen
            imgPhoto.setImageBitmap(bitmap)
            imgPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
            imgPhoto.imageTintList = null  // Quitar el tint gris

            // Cambiar el tamaño para que ocupe todo el espacio
            val params = imgPhoto.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            params.width = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            params.height = 0
            params.dimensionRatio = "16:9"
            imgPhoto.layoutParams = params

            // Ocultar los textos instructivos
            findViewById<TextView>(R.id.tvAddPhotos).visibility = android.view.View.GONE
            findViewById<TextView>(R.id.tvMaxPhotos).visibility = android.view.View.GONE
        }
    }

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { imageUri ->
                // Configurar la imagen
                imgPhoto.setImageURI(imageUri)
                imgPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                imgPhoto.imageTintList = null  // Quitar el tint gris

                // Cambiar el tamaño para que ocupe todo el espacio
                val params = imgPhoto.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
                params.width = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
                params.height = 0
                params.dimensionRatio = "16:9"
                imgPhoto.layoutParams = params

                // Ocultar los textos instructivos
                findViewById<TextView>(R.id.tvAddPhotos).visibility = android.view.View.GONE
                findViewById<TextView>(R.id.tvMaxPhotos).visibility = android.view.View.GONE
            }
        }
    }

    private fun takePhoto() {
        cameraPreviewLauncher.launch(null)
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        selectImageLauncher.launch(intent)
    }
}