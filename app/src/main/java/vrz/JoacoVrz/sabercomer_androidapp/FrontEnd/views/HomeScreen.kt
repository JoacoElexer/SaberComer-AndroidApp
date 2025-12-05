package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components.HeaderComponent
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components.patientCard
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.navigation.Screen
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.PatientsViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: PatientsViewModel) {
    val pacientes by viewModel.pacientes.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val success by viewModel.successMessage.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.cargarPacientes()
    }

    val headerBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF006192),
            Color(0xFF5ABDEF)
        )
    )
    val secondBackground = Color(0xFF5ABDEF)
    
    // Global container
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CrearPaciente.route) },
                containerColor = Color.Transparent,
                contentColor = Color.White,
                modifier = Modifier.background(
                    brush = headerBackground,
                    shape = MaterialTheme.shapes.extraLarge
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Paciente")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {

            // 1. HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        brush = headerBackground,
                        shape = RoundedCornerShape(bottomEnd = 70.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                HeaderComponent("Lista de Pacientes", "Inicio")
            }

            Box(
                modifier = Modifier
                    .background(color = secondBackground)
                    .fillMaxWidth()
                    .weight(4f) // 4/5 del espacio (aprox)
            )
            {
                Column(
                    modifier = Modifier
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(topStart = 50.dp)
                        )
                        .fillMaxSize()
                ) {

                    // Mostrar errores o mensajes
                    error?.let { Text(it, color = Color.Red, modifier = Modifier.padding(16.dp)) }
                    success?.let { Text(it, color = Color.Green, modifier = Modifier.padding(16.dp)) }

                    // Indicador de Carga de Lista (Si está cargando)
                    if (isLoading && pacientes.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else if (pacientes.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay pacientes registrados.")
                        }
                    } else {
                        // LA LISTA DE PACIENTES
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(top = 8.dp),
                            verticalArrangement = Arrangement.Top
                        ) {
                            items(pacientes) { paciente ->
                                patientCard(
                                    nombre = paciente.nombre,
                                    telefono = paciente.telefono ?: "N/D",
                                    onDetalleClick = {
                                        navController.navigate(Screen.DetallePaciente.createRoute(paciente.id))
                                    },
                                    onDeleteClick = {
                                        // Aquí se debe agregar un diálogo de confirmación en la versión final
                                        viewModel.borrarPaciente(paciente.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
/*
@Preview(
    showBackground = true,
    showSystemUi = true
)

@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
*/