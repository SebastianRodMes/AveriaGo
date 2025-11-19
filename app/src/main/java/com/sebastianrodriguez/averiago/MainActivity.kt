package com.sebastianrodriguez.averiago

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.sebastianrodriguez.averiago.report.CreateReportStep1Activity
import Controller.UserController
import Controller.TicketController
import Entity.Ticket
import Entity.enums.TicketStatus
import Util
import com.sebastianrodriguez.averiago.report.ReportsActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var tvUserName: TextView

    private lateinit var userController: UserController
    private lateinit var ticketController: TicketController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar controladores
        userController = UserController(this)
        ticketController = TicketController(this)

        // Inicializar vistas
        bottomNavigation = findViewById(R.id.bottomNavigation)
        tvUserName = findViewById(R.id.tvUserName)

        // Cargar nombre del usuario
        loadUserName()

        // Configurar listeners
        setupBottomNavigation()
        setupButtons()

        // Cargar tickets del usuario y mostrar el último
        loadUserReports()
    }

    private fun loadUserName() {
        // Obtener userId de sesión
        val sessionPref = getSharedPreferences("AveriaguApp_session", MODE_PRIVATE)
        val currentUserId = sessionPref.getString("currentUserId", null)

        val fullName = try {
            val idToUse = currentUserId ?: userController.getAllUsers()?.firstOrNull()?.userId
            idToUse?.let { userController.getUserById(it)!!.fullName }
        } catch (e: Exception) {
            null
        } ?: run {
            // Fallback a shared prefs clásico (por compatibilidad)
            val sharedPref = getSharedPreferences("AveriaguApp", MODE_PRIVATE)
            sharedPref.getString("fullName", "Usuario")
        }

        tvUserName.text = fullName
    }

    private fun setupBottomNavigation() {
        // Marcar "Inicio" como seleccionado por defecto
        bottomNavigation.selectedItemId = R.id.nav_home

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Ya estamos en home, no hacer nada
                    true
                }
                R.id.nav_reportes -> {

                    Util.openActivity(this, ReportsActivity::class.java)
                    true
                }
                R.id.nav_crear -> {
                    // Abrir Activity para Crear Reporte
                    Util.openActivity(this, CreateReportStep1Activity::class.java)
                    true
                }
                R.id.nav_chat -> {
                    // Abrir Activity de Chat
                    Toast.makeText(this, "Chat - Próximamente", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_perfil -> {
                    // Abrir Activity de Perfil
                    Toast.makeText(this, "Perfil - Próximamente", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupButtons() {
        // Botón principal de reportar avería
        val btnReportAveria = findViewById<MaterialButton>(R.id.btnReportAveria)
        btnReportAveria.setOnClickListener {
            Util.openActivity(this, CreateReportStep1Activity::class.java)
        }

        // Botón de notificaciones
        val btnNotifications = findViewById<MaterialButton>(R.id.btnNotifications)
        btnNotifications.setOnClickListener {
            Toast.makeText(this, "Notificaciones - Próximamente", Toast.LENGTH_SHORT).show()
        }

        // Ver todos los reportes
        val tvVerTodos = findViewById<TextView>(R.id.tvVerTodos)
        tvVerTodos.setOnClickListener {
            Util.openActivity(this, ReportsActivity::class.java)
        }

        // Acceso rápido - Mis Reportes
        findViewById<View>(R.id.btnMisReportes).setOnClickListener {
            Util.openActivity(this, ReportsActivity::class.java)
        }

        // Acceso rápido - Noticias
        findViewById<View>(R.id.btnNoticias).setOnClickListener {
            Toast.makeText(this, "Noticias - Próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Siempre marcar "Inicio" cuando volvemos a esta actividad
        bottomNavigation.selectedItemId = R.id.nav_home

        // Refrescar nombre y últimos reportes al volver
        loadUserName()
        loadUserReports()
    }

    private fun loadUserReports() {
        // Obtener userId de la sesión
        val sessionPref = getSharedPreferences("AveriaguApp_session", MODE_PRIVATE)
        val currentUserId = sessionPref.getString("currentUserId", null)

        // Si no hay sesión, nos quedamos con el primer usuario registrado (solo para pruebas)
        val userId = currentUserId ?: run {
            try {
                userController.getAllUsers()?.firstOrNull()?.userId
            } catch (e: Exception) {
                null
            }
        }

        val cardReporte = findViewById<View?>(R.id.cardReporte)
        val tvCaseNumber = findViewById<TextView?>(R.id.tvCaseNumber_main)
        val chipStatus = findViewById<Chip?>(R.id.chipStatus_main)
        val tvDescription = findViewById<TextView?>(R.id.tvDescription_main)
        val tvDate = findViewById<TextView?>(R.id.tvDate_main)

        if (userId == null) {
            // No hay usuario -> ocultamos la tarjeta
            cardReporte?.visibility = View.GONE
            return
        }

        // Obtener tickets del usuario
        val tickets: List<Ticket>? = try {
            ticketController.getTicketsByUserId(userId)
        } catch (e: Exception) {
            null
        }

        if (tickets.isNullOrEmpty()) {
            // No hay tickets -> ocultamos la tarjeta
            cardReporte?.visibility = View.GONE
            return
        }

        // Tomamos el último ticket por createdAt
        val last = tickets.maxByOrNull { it.createdAt } ?: tickets.first()
        cardReporte?.visibility = View.VISIBLE

        // Mostrar número de caso (puedes usar un formato distinto si quieres)
        tvCaseNumber?.text = "Caso ${last.getFormattedId()}"

        // Mostrar descripción corta (category - subcategory)
        tvDescription?.text = "${last.category} - ${last.subcategory}"

        // Mostrar estado y color
        val status = last.status
        chipStatus?.text = status.displayName
        try {
            val colorInt = Color.parseColor(status.color.takeIf { it.isNotBlank() } ?: "#E5E7EB")
            chipStatus?.chipBackgroundColor = android.content.res.ColorStateList.valueOf(colorInt)
        } catch (e: Exception) {
            // fallback
        }

        // Mostrar fecha legible
        val fmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        tvDate?.text = fmt.format(Date(last.createdAt))
    }
}