package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views.testScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.Paciente
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.PatientsViewModel
import java.util.UUID

@Composable
fun TestPacientesScreen(viewModel: PatientsViewModel) {
    val pacientes by viewModel.pacientes.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val success by viewModel.successMessage.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Crear paciente dummy
                val dummy = Paciente(
                    id = UUID.randomUUID().toString().substring(0, 8), // ID corto al azar
                    nombre = "Paciente Test ${System.currentTimeMillis() % 1000}",
                    telefono = "1234567890",
                    fechaNacimiento = "1990-01-01",
                    fechaInicio = "2023-12-01"
                )
                viewModel.crearPaciente(dummy)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("TEST: PACIENTES (${pacientes.size})", style = MaterialTheme.typography.headlineSmall)

            if (isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            error?.let { Text(it, color = Color.Red) }
            success?.let { Text(it, color = Color.Green) }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
                items(pacientes) { p ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(p.nombre, style = MaterialTheme.typography.bodyLarge)
                            Text("ID: ${p.id}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { viewModel.borrarPaciente(p.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}