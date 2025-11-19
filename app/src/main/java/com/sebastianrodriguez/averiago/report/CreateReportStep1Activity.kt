package com.sebastianrodriguez.averiago.report

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.sebastianrodriguez.averiago.MainActivity
import com.sebastianrodriguez.averiago.R
import Util

class CreateReportStep1Activity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_report_step1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        setupServiceCards()
        setupBottomNavigation()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish() // Volver a MainActivity
        }
    }

    private fun setupServiceCards() {
        // Card Internet -> pasamos el slug (coincide con CategoryMemoryDataManager)
        val cardInternet = findViewById<MaterialCardView>(R.id.cardInternet)
        cardInternet.setOnClickListener {
            // Enviar slug en vez del nombre
            navigateToStep2("internet")
        }

        // Card Television
        val cardTelevision = findViewById<MaterialCardView>(R.id.cardTelevision)
        cardTelevision.setOnClickListener {
            navigateToStep2("television")
        }

        // Card Teléfono (slug definido en tu CategoryMemoryDataManager como "telefonia_movil")
        val cardPhone = findViewById<MaterialCardView>(R.id.cardPhone)
        cardPhone.setOnClickListener {
            navigateToStep2("telefonia_movil")
        }
    }

    private fun navigateToStep2(serviceSlug: String) {
        val intent = Intent(this, CreateReportStep2Activity::class.java)
        // Nota: ahora pasamos el SLUG para búsquedas consistentes
        intent.putExtra("SERVICE_SLUG", serviceSlug)
        startActivity(intent)
    }

    private fun setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_crear

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Util.openActivity(this, MainActivity::class.java)
                    finish()
                    true
                }
                R.id.nav_reportes -> {
                    // Util.openActivity(this, ReportesActivity::class.java)
                    // finish()
                    true
                }
                R.id.nav_crear -> {
                    // Ya estamos aquí
                    true
                }
                R.id.nav_chat -> {
                    // Chat activity
                    true
                }
                R.id.nav_perfil -> {
                    // Util.openActivity(this, PerfilActivity::class.java)
                    // finish()
                    true
                }
                else -> false
            }
        }
    }
}