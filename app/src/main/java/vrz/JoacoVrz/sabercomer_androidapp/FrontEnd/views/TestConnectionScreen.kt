package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views

import androidx.compose.runtime.Composable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import vrz.JoacoVrz.sabercomer_androidapp.BackEnd.ApiService
import vrz.JoacoVrz.sabercomer_androidapp.BackEnd.RetrofitClient
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.Paciente

@Composable
fun TestConnectionScreen() {
    // 1. Estado de la UI
    var pacientesList by remember { mutableStateOf<List<Paciente>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Scope para corutinas (operaciones asíncronas)
    val scope = rememberCoroutineScope()

    // 2. Función para obtener datos
    fun fetchData() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                // Creamos la instancia del servicio usando nuestro RetrofitClient
                val apiService = RetrofitClient.instance.create(ApiService::class.java)

                // Hacemos la llamada
                val response = apiService.getPacientes()

                if (response.isSuccessful) {
                    // Si es 200 OK, guardamos la lista (o lista vacía si es null)
                    pacientesList = response.body() ?: emptyList()
                    Log.d("API_TEST", "Pacientes recibidos: ${pacientesList.size}")
                } else {
                    errorMessage = "Error: ${response.code()} - ${response.message()}"
                    Log.e("API_TEST", "Error en respuesta: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Fallo de conexión: ${e.message}"
                Log.e("API_TEST", "Excepción: ", e)
            } finally {
                isLoading = false
            }
        }
    }

    // 3. UI Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Prueba de Conexión API", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { fetchData() }) {
            Text("Recargar Datos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else if (errorMessage != null) {
            Text(text = errorMessage!!, color = Color.Red)
            Text(text = "Revisa que Ngrok esté activo y la URL actualizada en RetrofitClient", style = MaterialTheme.typography.bodySmall)
        } else {
            // Lista de Pacientes
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pacientesList) { paciente ->
                    PacienteCard(paciente)
                }
            }
        }
    }
}

@Composable
fun PacienteCard(paciente: Paciente) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(text = paciente.nombre, style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Text(text = "Tel: ${paciente.telefono ?: "Sin número"}", style = MaterialTheme.typography.bodyMedium)

            // Ejemplo de acceso a subdocumento
            if (paciente.controlDePeso?.pesoInicio != null) {
                Text(
                    text = "Peso Inicio: ${paciente.controlDePeso.pesoInicio} kg",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}