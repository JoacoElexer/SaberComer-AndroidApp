package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object CrearPaciente : Screen("crear_paciente_screen")
    // Esta ruta acepta argumentos (el ID del paciente)
    object DetallePaciente : Screen("detalle_paciente_screen/{pacienteId}") {
        fun createRoute(pacienteId: String) = "detalle_paciente_screen/$pacienteId"
    }
    object Calendario : Screen("calendario_screen")
    object Configuracion : Screen("configuracion_screen")
}