package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models

import com.google.gson.annotations.SerializedName

data class Paciente(
    // --- Identificación y Datos Demográficos ---
    val id: String,
    val nombre: String,
    val direccion: String? = "",
    val ciudad: String? = "",
    val telefono: String?,
    val fechaNacimiento: String, // String ISO desde la API
    val fechaInicio: String,     // String ISO desde la API
    val ocupacion: String? = "",
    // --- Sub-documentos ---
    val ahf: AntecedentesHeredoFamiliares? = null,
    val apnp: AntecedentesPersonalesNoPatologicos? = null,
    val app: AntecedentesPersonalesPatologicos? = null,
    val ago: AntecedentesGinecoObstetricos? = null,
    val cdp: ControlDePeso? = null,
    val notasAdicionales: String? = ""
)

// --- Antecedentes Heredofamiliares (Booleanos) ---
data class AntecedentesHeredoFamiliares (
    @SerializedName("HTA") val hta: Boolean? = false, // Hipertensión
    @SerializedName("DM") val dm: Boolean? = false, // Diabetes Mellitus
    @SerializedName("CA") val ca: Boolean? = false, // Cáncer
    val tiroides: Boolean? = false,
    val cardiopatias: Boolean? = false,
    @SerializedName("ahfOtros") val ahfOtros: String? = "Ninguno"
)


// --- Antecedentes Personales no Patologicos (Booleanos) ---
data class AntecedentesPersonalesNoPatologicos (
    val tabaquismo: Boolean = false,
    val drogas: Boolean = false,
    @SerializedName("OH") val oh: Boolean = false  // Alcoholismo
)



// --- Antecedentes Personales Patologicos ---
data class AntecedentesPersonalesPatologicos(
    val enfermedadesPadecidas: String? = "Ninguna",
    val antecedentesTraumaticos: String? = "Ninguno",
    val antecedentesQuirurgicos: String? = "Ninguno",
    val alergiasMedicamentos: String? = "Ninguna",
    val alergiasAlimentos: String? = "Ninguna",
)

data class ControlDePeso(
    val antecedentesTratamientosCP: String? = "",
    val pesoInicio: Double? = 0.0,
    val pesoIdeal: Double? = 0.0,
    val estatura: Double? = 0.0,
    val suIdeal: Double? = 0.0
)

data class AntecedentesGinecoObstetricos(
    @SerializedName("G") val g: String? = "",
    @SerializedName("P") val p: String? = "",
    @SerializedName("C") val c: String? = "",
    @SerializedName("A") val a: String? = "",
    @SerializedName("FUR") val fur: String? = "", // Fecha Última Regla
    @SerializedName("AgoOtros") val agoOtros: String? = ""
)