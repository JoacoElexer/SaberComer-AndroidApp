package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views.testScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.LoginViewModel

@Composable
fun TestLoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.loginState.collectAsStateWithLifecycle()

    var correo by remember { mutableStateOf("test@example.com") } // Valor por defecto para probar rápido
    var pin by remember { mutableStateOf("1234") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("TEST: LOGIN", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") })
        OutlinedTextField(value = pin, onValueChange = { pin = it }, label = { Text("PIN") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.login(correo, pin) }) {
            Text("Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val s = state) {
            is LoginViewModel.LoginState.Loading -> CircularProgressIndicator()
            is LoginViewModel.LoginState.Error -> Text("Error: ${s.mensaje}", color = Color.Red)
            is LoginViewModel.LoginState.Success -> {
                Text("¡Login Exitoso!", color = Color.Green, style = MaterialTheme.typography.titleLarge)
                Text("Usuario: ${s.usuario.usuario}")
                Text("Rol: ${s.usuario.role?.rol}")
            }
            else -> Text("Esperando credenciales...")
        }
    }
}