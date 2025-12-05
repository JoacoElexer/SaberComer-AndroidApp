package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models

import com.google.gson.annotations.SerializedName

// Lo que recibimos de la API (Perfil de usuario)
data class Usuario(
    @SerializedName("_id") val mongoId: String,
    val usuario: String,
    val correo: String,
    val role: UserRole?
)

// Subdocumento de rol
data class UserRole(
    val rol: String // "dev", "admin", "user"
)

// Lo que enviamos para iniciar sesión
data class LoginRequest(
    val correo: String,
    val pin: String
)

// Respuesta específica del Login (A veces devuelve token + usuario)
// Asumiremos que devuelve el objeto Usuario por ahora.
// Si tu backend devuelve { token: "...", user: { ... } }, habría que ajustar esto.