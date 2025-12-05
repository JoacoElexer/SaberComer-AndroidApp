package vrz.JoacoVrz.sabercomer_androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import retrofit2.create
import vrz.JoacoVrz.sabercomer_androidapp.BackEnd.ApiService
import vrz.JoacoVrz.sabercomer_androidapp.BackEnd.RetrofitClient
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.navigation.Screen
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.repository.SaberComerRepository
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.ControlClinicoViewModel
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.LoginViewModel
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.PatientsViewModel
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.RecordMedidasViewModel
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.SaberComerViewModelFactory
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views.HomeScreen
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views.LoginScreen
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views.testScreens.TestConnectionScreen
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views.testScreens.TestControlScreen
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views.testScreens.TestLoginScreen
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views.testScreens.TestMedidasScreen
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views.testScreens.TestPacientesScreen
import vrz.JoacoVrz.sabercomer_androidapp.ui.theme.SaberComerAndroidAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val repo = SaberComerRepository(apiService)
        val factory: ViewModelProvider.Factory = SaberComerViewModelFactory(repo)
        enableEdgeToEdge()
        setContent {
            SaberComerAndroidAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route,
                    // Animaciones globales (Slide lateral)
                ) {
                    // 1. PANTALLA LOGIN
                    composable(Screen.Login.route) {
                        val loginViewModel: LoginViewModel = viewModel(factory = factory)

                        LoginScreen(
                            viewModel = loginViewModel,
                            onLoginSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // 2. PANTALLA HOME (Lista de Pacientes)
                    composable(Screen.Home.route) {
                        val homeViewModel: PatientsViewModel = viewModel(factory = factory)

                        HomeScreen(navController, homeViewModel)
                    }

                    // 3. CREAR PACIENTE
                    composable(Screen.CrearPaciente.route) {
                        CrearPacienteScreenPlaceholder(navController)
                    }

                    // 4. DETALLE PACIENTE (Recibe ID)
                    composable(
                        route = Screen.DetallePaciente.route,
                        arguments = listOf(navArgument("pacienteId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val pacienteId = backStackEntry.arguments?.getString("pacienteId") ?: ""
                        DetallePacienteScreenPlaceholder(navController, pacienteId)
                    }

                    // 5. CALENDARIO
                    composable(Screen.Calendario.route) {
                        CalendarioScreenPlaceholder(navController)
                    }

                    // 6. CONFIGURACIÓN
                    composable(Screen.Configuracion.route) {
                        ConfiguracionScreenPlaceholder(navController)
                    }
                }
                // ===== PANTALLAS DE PRUEBA ===== //
                // --- PRUEBA 1: LOGIN ---
                // val viewModel: LoginViewModel = viewModel(factory = factory)
                // TestLoginScreen(viewModel)

                // --- PRUEBA 2: PACIENTES ---
                // val viewModel: PatientsViewModel = viewModel(factory = factory)
                // TestPacientesScreen(viewModel)

                // --- PRUEBA 3: MEDIDAS ---
                // val viewModel: RecordMedidasViewModel = viewModel(factory = factory)
                // TestMedidasScreen(viewModel)

                // --- PRUEBA 4: CONTROL CLÍNICO ---
                // val viewModel: ControlClinicoViewModel = viewModel(factory = factory)
                // TestControlScreen(viewModel)
            }
        }
    }
}

// ==========================================
// PANTALLAS PLACEHOLDER (TEMPORALES)
// ==========================================

@Composable
fun LoginScreenPlaceholder(viewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onLoginSuccess) {
            Text("Simular Login Exitoso -> Ir a Home")
        }
    }
}

@Composable
fun HomeScreenPlaceholder(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.layout.Column {
            Text("Pantalla Home (Lista Pacientes)")
            Button(onClick = { navController.navigate(Screen.CrearPaciente.route) }) {
                Text("Ir a Crear Paciente")
            }
            Button(onClick = { navController.navigate(Screen.DetallePaciente.createRoute("123-prueba")) }) {
                Text("Ir a Detalle de Paciente ID: 123")
            }
            Button(onClick = { navController.navigate(Screen.Calendario.route) }) {
                Text("Ir a Calendario")
            }
            Button(onClick = { navController.navigate(Screen.Configuracion.route) }) {
                Text("Ir a Configuración")
            }
        }
    }
}

@Composable
fun CrearPacienteScreenPlaceholder(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.layout.Column {
            Text("Pantalla Crear Paciente")
            Button(onClick = { navController.popBackStack() }) {
                Text("Guardar y Volver")
            }
        }
    }
}

@Composable
fun DetallePacienteScreenPlaceholder(navController: NavController, pacienteId: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.layout.Column {
            Text("Detalles del Paciente: $pacienteId")
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }
        }
    }
}

@Composable
fun CalendarioScreenPlaceholder(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla Calendario")
    }
}

@Composable
fun ConfiguracionScreenPlaceholder(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla Configuración")
    }
}