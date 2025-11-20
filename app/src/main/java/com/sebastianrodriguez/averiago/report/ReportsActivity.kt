package com.sebastianrodriguez.averiago.report

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.sebastianrodriguez.averiago.MainActivity
import com.sebastianrodriguez.averiago.R
import Controller.TicketController
import Controller.UserController
import Entity.Ticket
import Entity.enums.TicketStatus
import Util

class ReportsActivity : AppCompatActivity() {

    private lateinit var rvReports: RecyclerView
    private lateinit var emptyState: View
    private lateinit var reportAdapter: ReportAdapter
    private lateinit var bottomNavigation: BottomNavigationView

    private lateinit var ticketController: TicketController
    private lateinit var userController: UserController

    private var allTickets: List<Ticket> = emptyList()
    private var currentFilter: FilterType = FilterType.ALL

    enum class FilterType {
        ALL, ACTIVE, RESOLVED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reports)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar controladores
        ticketController = TicketController(this)
        userController = UserController(this)

        // Inicializar vistas
        rvReports = findViewById(R.id.rvReports)
        emptyState = findViewById(R.id.emptyState)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Configurar RecyclerView
        setupRecyclerView()

        // Configurar filtros
        setupFilters()

        // Configurar bottom navigation
        setupBottomNavigation()

        // Cargar reportes
        loadReports()
    }

    private fun setupRecyclerView() {
        reportAdapter = ReportAdapter(emptyList()) { ticket ->
            // Al hacer click en un reporte, abrir los detalles
            Toast.makeText(this, "Ver detalles del caso ${ticket.getFormattedId()}", Toast.LENGTH_SHORT).show()
            // Aquí se podria  navegar a la pantalla de detalles:
            // val intent = Intent(this, ReportDetailActivity::class.java)
            // intent.putExtra("TICKET_ID", ticket.ticketId)
            // startActivity(intent)
        }

        rvReports.apply {
            layoutManager = LinearLayoutManager(this@ReportsActivity)
            adapter = reportAdapter
        }
    }

    private fun setupFilters() {
        val chipTodos = findViewById<Chip>(R.id.chipTodos)
        val chipActivos = findViewById<Chip>(R.id.chipActivos)
        val chipResueltos = findViewById<Chip>(R.id.chipResueltos)

        chipTodos.setOnClickListener {
            currentFilter = FilterType.ALL
            filterReports()
        }

        chipActivos.setOnClickListener {
            currentFilter = FilterType.ACTIVE
            filterReports()
        }

        chipResueltos.setOnClickListener {
            currentFilter = FilterType.RESOLVED
            filterReports()
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_reportes

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Util.openActivity(this, MainActivity::class.java)
                    finish()
                    true
                }
                R.id.nav_reportes -> {
                    // Ya estamos aquí
                    true
                }
                R.id.nav_crear -> {
                    Util.openActivity(this, CreateReportStep1Activity::class.java)
                    true
                }
                R.id.nav_chat -> {
                    Toast.makeText(this, "Chat - Próximamente", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_perfil -> {
                    Toast.makeText(this, "Perfil - Próximamente", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadReports() {
        // Obtener userId de sesión
        val sessionPref = getSharedPreferences("AveriaguApp_session", MODE_PRIVATE)
        val currentUserId = sessionPref.getString("currentUserId", null)

        val userId = currentUserId ?: run {
            try {
                userController.getAllUsers()?.firstOrNull()?.userId
            } catch (e: Exception) {
                null
            }
        }

        if (userId == null) {
            showEmptyState()
            return
        }

        // Cargar tickets del usuario
        allTickets = try {
            ticketController.getTicketsByUserId(userId) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        // Ordenar por fecha (más reciente primero)
        allTickets = allTickets.sortedByDescending { it.createdAt }

        filterReports()
    }

    private fun filterReports() {
        val filteredTickets = when (currentFilter) {
            FilterType.ALL -> allTickets
            FilterType.ACTIVE -> allTickets.filter {
                it.status != TicketStatus.SOLVED && it.status != TicketStatus.CLOSED
            }
            FilterType.RESOLVED -> allTickets.filter {
                it.status == TicketStatus.SOLVED || it.status == TicketStatus.CLOSED
            }
        }

        if (filteredTickets.isEmpty()) {
            showEmptyState()
        } else {
            hideEmptyState()
            reportAdapter.updateTickets(filteredTickets)
        }
    }

    private fun showEmptyState() {
        rvReports.visibility = View.GONE
        emptyState.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        rvReports.visibility = View.VISIBLE
        emptyState.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        // Recargar reportes al volver a la actividad
        loadReports()
    }
}