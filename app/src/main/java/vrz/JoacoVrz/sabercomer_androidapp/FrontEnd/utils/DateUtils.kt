package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    private val formatter = DateTimeFormatter
        .ofPattern("dd 'de' MMMM 'de' yyyy", Locale("es", "MX"))
        .withZone(ZoneId.systemDefault())

    private val formatterCorto = DateTimeFormatter
        .ofPattern("dd/MM/yyyy", Locale("es", "MX"))
        .withZone(ZoneId.systemDefault())

    /**
     * Convierte un timestamp ISO 8601 a formato largo.
     * Ejemplo: "2026-01-15T00:00:00.000Z" -> "15 de enero de 2026"
     */
    fun formatearFecha(isoString: String?): String {
        if (isoString.isNullOrBlank()) return "Sin fecha"
        return try {
            val instant = Instant.parse(isoString)
            formatter.format(instant)
        } catch (e: Exception) {
            // Si ya viene como "yyyy-MM-dd" sin hora, lo manejamos también
            try {
                val localDate = java.time.LocalDate.parse(isoString)
                localDate.format(
                    DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale("es", "MX"))
                )
            } catch (e2: Exception) {
                isoString // Si falla todo, mostramos el string original
            }
        }
    }

    /**
     * Convierte un timestamp ISO 8601 a formato corto.
     * Ejemplo: "2026-01-15T00:00:00.000Z" -> "15/01/2026"
     */
    fun formatearFechaCorta(isoString: String?): String {
        if (isoString.isNullOrBlank()) return "Sin fecha"
        return try {
            val instant = Instant.parse(isoString)
            formatterCorto.format(instant)
        } catch (e: Exception) {
            try {
                val localDate = java.time.LocalDate.parse(isoString)
                localDate.format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("es", "MX"))
                )
            } catch (e2: Exception) {
                isoString
            }
        }
    }
}