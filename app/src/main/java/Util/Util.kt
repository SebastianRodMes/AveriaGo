import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat

class Util {
    companion object{
        fun OpenActivity(context: Context, objClassActivity: Class<*>){
            val objIntent = Intent(context, objClassActivity)
            //usar el objeto context que se esta recibiendo como parámetro en la función. El context (que será la Activity cuando se llame a esta función)
            // es el que tiene la capacidad de iniciar otras actividades.
            context.startActivity(objIntent)
        }



        /**
         * Converts a timestamp (Long) into a something readable date and time string.
         *
         * The desired output format ("dd/MM/yyyy HH:mm").
         *
         */
        fun formatTimestamp(timestamp: Long, format: String = "dd/MM/yyyy HH:mm a"): String {
            if (timestamp == 0L) return "N/A"
            val sdf = SimpleDateFormat(format, androidx.compose.ui.text.intl.Locale.getDefault())
            val date = com.google.type.Date(timestamp)
            return sdf.format(date)
        }
    }
}