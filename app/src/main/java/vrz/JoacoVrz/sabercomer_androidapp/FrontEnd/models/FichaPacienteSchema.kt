package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models

import com.google.gson.annotations.SerializedName

data class Paciente(
    // --- Identificación y Datos Demográficos ---
    val id: String,
    val nombre: String,
    val direccion: String? = "",
    val noCasa: Int? = null,
    val colonia: String? = "",
    val ciudad: String? = "",
    val telefono: String?,
    val fechaNacimiento: String, // String ISO desde la API
    val fechaInicio: String,     // String ISO desde la API
    val ocupacion: String? = "",

    // --- Antecedentes Heredofamiliares y Patológicos (Booleanos) ---
    @SerializedName("HTA") val hta: Boolean = false,       // Hipertensión
    @SerializedName("DM") val dm: Boolean = false,         // Diabetes Mellitus
    @SerializedName("CA") val ca: Boolean = false,         // Cáncer
    val tiroides: Boolean = false,
    val cardiopatias: Boolean = false,
    @SerializedName("AHFOtros") val ahfOtros: String? = "Ninguno",

    // --- Hábitos ---
    val tabaquismo: Boolean = false,
    val drogas: Boolean = false,
    @SerializedName("OH") val oh: Boolean = false,         // Alcoholismo

    // --- Antecedentes Personales ---
    val enfermedadesPadecidas: String? = "Ninguna",
    val antecedentesTraumaticos: String? = "Ninguno",
    val antecedentesQuirurgicos: String? = "Ninguno",
    val alergiasMedicamentos: String? = "Ninguna",
    val alergiasAlimentos: String? = "Ninguna",

    // --- Sub-documentos ---
    val antecedentesGinecoObstetricos: AntecedentesGinecoObstetricos? = null,
    val controlDePeso: ControlDePeso? = null,

    val notasAdicionales: String? = ""
)

data class ControlDePeso(
    val antecedentesTratamientosCP: String? = "",
    val pesoInicio: Double? = null,
    val pesoIdeal: Double? = null,
    val estatura: Double? = null,
    val suIdeal: Double? = null
)

data class AntecedentesGinecoObstetricos(
    @SerializedName("G") val g: String? = "",
    @SerializedName("P") val p: String? = "",
    @SerializedName("C") val c: String? = "",
    @SerializedName("A") val a: String? = "",
    @SerializedName("FUR") val fur: String? = "", // Fecha Última Regla
    @SerializedName("AgoOtros") val agoOtros: String? = ""
)