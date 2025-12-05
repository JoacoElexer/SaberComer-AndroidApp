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
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.MedidasCorporales
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.RecordMedidas
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.RecordMedidasViewModel

@Composable
fun TestMedidasScreen(viewModel: RecordMedidasViewModel) {
    val medidasList by viewModel.medidasList.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var pacienteIdInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("TEST: MEDIDAS", style = MaterialTheme.typography.headlineSmall)

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = pacienteIdInput,
                onValueChange = { pacienteIdInput = it },
                label = { Text("ID Paciente") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { viewModel.cargarMedidas(pacienteIdInput) }) {
                Text("Cargar")
            }
        }

        if (isLoading) CircularProgressIndicator()

        Button(
            onClick = {
                val dummy = RecordMedidas(
                    mongoId = "",
                    id = pacienteIdInput,
                    fecha = "2023-12-05",
                    medidas = MedidasCorporales(
                        busto = 90.0, abdomenAlto = 80.0, ombligo = 85.0, cadera = 100.0
                    ),
                    observaciones = "Medida Test Automática"
                )
                viewModel.crearMedida(dummy)
            },
            enabled = pacienteIdInput.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Agregar Medida Dummy")
        }

        LazyColumn {
            items(medidasList) { m ->
                Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    Row(modifier = Modifier.padding(8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Fecha: ${m.fecha}")
                            Text("Cintura (Ombligo): ${m.medidas?.ombligo} cm")
                        }
                        IconButton(onClick = { viewModel.borrarMedida(m.mongoId) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar")
                        }
                    }
                }
            }
        }
    }
}