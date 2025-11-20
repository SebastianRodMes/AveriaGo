package com.sebastianrodriguez.averiago.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.TextView
import com.sebastianrodriguez.averiago.R

class LoadingDialog(private val context: Context) {

    private var dialog: Dialog? = null

    fun show(message: String = "Cargando...") {
        // Inflar el layout del loading
        val view = LayoutInflater.from(context).inflate(R.layout.loading, null)


        val tvMessage = view.findViewById<TextView>(R.id.tvLoadingMessage)
        tvMessage.text = message

        // Crear el diálogo
        dialog = Dialog(context)
        dialog?.setContentView(view)
        dialog?.setCancelable(false) // No se puede cerrar tocando afuera
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
    }

    fun show(message: String = "Cargando...", durationMillis: Long, onFinish: () -> Unit) {
        show(message) // Mostrar el dialog

        // Después de la duración especificada, ocultar y ejecutar el callback
        Handler(Looper.getMainLooper()).postDelayed({
            hide()
            onFinish() // Ejecutar lo que venga después
        }, durationMillis)
    }

    fun getDialog(): Dialog? {
        return dialog
    }


    fun hide() {
        dialog?.dismiss()
        dialog = null
    }
}