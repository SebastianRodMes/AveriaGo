
import java.text.SimpleDateFormat
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class Util {
    companion object{
        fun OpenActivity(context: Context, objClassActivity: Class<*>){
            val objIntent = Intent(context, objClassActivity)
            //usar el objeto context que se esta recibiendo como parámetro en la función. El context (que será la Activity cuando se llame a esta función)
            // es el que tiene la capacidad de iniciar otras actividades.
            context.startActivity(objIntent)
        }




    }
}