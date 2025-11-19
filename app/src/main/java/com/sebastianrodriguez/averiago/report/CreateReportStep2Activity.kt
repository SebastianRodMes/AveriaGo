package com.sebastianrodriguez.averiago.report

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.sebastianrodriguez.averiago.R
import Controller.CategoryController
import Entity.Subcategory

class CreateReportStep2Activity : AppCompatActivity() {

    private lateinit var serviceSlug: String
    private lateinit var tvSelectedService: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var rgProblemType: RadioGroup
    private lateinit var etDescription: TextInputEditText

    private val categoryController: CategoryController by lazy { CategoryController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_report_step2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Leer el SLUG enviado desde Step1
        serviceSlug = intent.getStringExtra("SERVICE_SLUG") ?: "internet"

        setupToolbar()
        setupViews()

        // Intentamos cargar la categoría desde CategoryController
        val category = try {
            categoryController.getCategoryBySlug(serviceSlug)
        } catch (e: Exception) {
            null
        }

        if (category != null) {
            // Mostrar nombre legible
            tvSelectedService.text = "Servicio: ${category.name}"
            // Cambiar la pregunta según la categoría
            tvQuestion.text = "¿Qué problema tienes con tu ${category.name}?"
            // Poblar las subcategorías dinámicamente
            loadSubcategories(category.subcategories)
        } else {
            // Fallback: si por alguna razón no se encuentra la categoría, usamos la lógica anterior
            tvSelectedService.text = "Servicio: ${serviceSlug.replace('_', ' ').capitalize()}"
            tvQuestion.text = "¿Qué tipo de problema tienes?"
            loadFallbackOptions()
        }

        setupContinueButton()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupViews() {
        tvSelectedService = findViewById(R.id.tvSelectedService)
        tvQuestion = findViewById(R.id.tvQuestion)
        rgProblemType = findViewById(R.id.rgProblemType)
        etDescription = findViewById(R.id.etDescription)
    }

    private fun loadSubcategories(subcategories: List<Subcategory>) {
        rgProblemType.removeAllViews()
        if (subcategories.isEmpty()) {
            // Si no hay subcategorías definidas, mostramos una opción "Otro"
            val radioButton = com.google.android.material.radiobutton.MaterialRadioButton(this).apply {
                text = "Otro"
                id = View.generateViewId()
            }
            rgProblemType.addView(radioButton)
            return
        }

        subcategories.sortedBy { it.order }.forEach { sub ->
            val radioButton = com.google.android.material.radiobutton.MaterialRadioButton(this).apply {
                text = sub.name
                id = View.generateViewId()
                setPadding( dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12) )
                textSize = 14f
            }
            rgProblemType.addView(radioButton)
        }
    }

    // El antiguo fallback (tu lista hardcodeada) en caso de que la categoría no exista
    private fun loadFallbackOptions() {
        val options = when (serviceSlug.toLowerCase()) {
            "internet" -> listOf(
                "Sin conexión",
                "Velocidad lenta",
                "Conexión intermitente",
                "No puedo acceder a ciertas páginas",
                "Otro"
            )
            "television" -> listOf(
                "Sin señal",
                "Imagen congelada o pixelada",
                "Problemas de audio",
                "Canales faltantes",
                "Decodificador no enciende",
                "Control remoto no funciona",
                "Otro"
            )
            "teléfono", "telefonia_movil" -> listOf(
                "Sin tono de marcado",
                "Estática o ruido en la línea",
                "No puedo hacer llamadas",
                "No puedo recibir llamadas",
                "Las llamadas se cortan",
                "Otro"
            )
            else -> listOf("Otro")
        }

        rgProblemType.removeAllViews()
        options.forEach { option ->
            val radioButton = com.google.android.material.radiobutton.MaterialRadioButton(this).apply {
                text = option
                id = View.generateViewId()
                setPadding( dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12) )
                textSize = 14f
            }
            rgProblemType.addView(radioButton)
        }
    }

    // Helper para convertir dp a pixels
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun setupContinueButton() {
        val btnContinue = findViewById<MaterialButton>(R.id.btnContinue)
        btnContinue.setOnClickListener {
            if (validateInputs()) {
                val problemType = getSelectedProblemType()
                val description = etDescription.text.toString().trim()

                navigateToStep3(problemType, description)
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (rgProblemType.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Por favor selecciona un tipo de problema", Toast.LENGTH_SHORT).show()
            return false
        }

        if (etDescription.text.isNullOrBlank()) {
            Toast.makeText(this, "Por favor describe el problema en detalle", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun getSelectedProblemType(): String {
        val selectedId = rgProblemType.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(selectedId)
        return radioButton.text.toString()
    }

    private fun navigateToStep3(problemType: String, description: String) {
        val intent = android.content.Intent(this, CreateReportStep3Activity::class.java)
        // Mantengo compatibilidad con lo que espera Step3: SERVICE_TYPE ahora será el slug (serviceSlug)
        intent.putExtra("SERVICE_TYPE", serviceSlug)
        intent.putExtra("PROBLEM_TYPE", problemType)
        intent.putExtra("DESCRIPTION", description)
        startActivity(intent)
    }
}