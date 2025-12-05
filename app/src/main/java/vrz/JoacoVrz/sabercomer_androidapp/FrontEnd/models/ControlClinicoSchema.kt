package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models

import com.google.gson.annotations.SerializedName

data class ControlClinico(
    @SerializedName("_id") val mongoId: String,
    val id: String,
    val fecha: String,
    val peso: Double?,
    val tx: String?,
    val guia: String?,
    val observaciones: String?,
    val calificacion: Int?,
    val opcionesControl: OpcionesControl?
)

data class OpcionesControl(
    val mesoterapia: Boolean = false,
    val acupuntura: Boolean = false,
    val ejercicios: Boolean = false,
    val agua: Boolean = false
)