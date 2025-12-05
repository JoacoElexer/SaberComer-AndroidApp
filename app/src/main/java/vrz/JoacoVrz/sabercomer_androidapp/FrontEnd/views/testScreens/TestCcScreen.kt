package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views.testScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.ControlClinico
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.ControlClinicoViewModel

@Composable
fun TestControlScreen(viewModel: ControlClinicoViewModel) {
    val controles by viewModel.controles.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    // Estado local para el ID del paciente a probar
    var pacienteIdInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("TEST: CONTROL CLÍNICO", style = MaterialTheme.typography.headlineSmall)

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = pacienteIdInput,
                onValueChange = { pacienteIdInput = it },
                label = { Text("ID Paciente") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { viewModel.cargarControles(pacienteIdInput) }) {
                Text("Cargar")
            }
        }

        if (isLoading) CircularProgressIndicator()

        Button(
            onClick = {
                // Agregar Control Dummy
                val dummy = ControlClinico(
                    mongoId = "", // El backend lo genera
                    id = pacienteIdInput,
                    fecha = "2023-12-05",
                    peso = 70.5 + (Math.random() * 5),
                    tx = "Dieta Test",
                    guia = "Guia 1",
                    observaciones = "Prueba generada",
                    calificacion = 10,
                    opcionesControl = null
                )
                viewModel.crearControl(dummy)
            },
            enabled = pacienteIdInput.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Agregar Control Dummy")
        }

        LazyColumn {
            items(controles) { c ->
                Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    Row(modifier = Modifier.padding(8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Fecha: ${c.fecha}")
                            Text("Peso: ${c.peso}kg - Calif: ${c.calificacion}")
                        }
                        IconButton(onClick = { viewModel.borrarControl(c.mongoId) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar")
                        }
                    }
                }
            }
        }
    }
}