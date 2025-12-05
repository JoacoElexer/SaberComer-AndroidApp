package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models

import com.google.gson.annotations.SerializedName

data class RecordMedidas(
    @SerializedName("_id") val mongoId: String,
    val id: String,
    val fecha: String,
    val medidas: MedidasCorporales?,
    val observaciones: String?
)

data class MedidasCorporales(
    val busto: Double?,
    val abdomenAlto: Double?,
    val ombligo: Double?,
    val cadera: Double?
)
