package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components.HeaderComponent
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.AntecedentesGinecoObstetricos
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.ControlDePeso
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.Paciente
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.PatientsViewModel
import java.util.UUID
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextFieldDefaults
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.AntecedentesHeredoFamiliares
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.AntecedentesPersonalesNoPatologicos
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.AntecedentesPersonalesPatologicos
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CrearPacienteScreen(navController: NavController, viewModel: PatientsViewModel) {

    val todayDate = remember {
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    // Inicializamos el objeto con todas sus subclases para evitar NullPointer al editar
    var nuevoPaciente by remember {
        mutableStateOf(
            Paciente(
                id = UUID.randomUUID().toString(),
                nombre = "",
                fechaNacimiento = "",
                fechaInicio = todayDate,
                ahf = AntecedentesHeredoFamiliares(),
                apnp = AntecedentesPersonalesNoPatologicos(),
                app = AntecedentesPersonalesPatologicos(),
                ago = AntecedentesGinecoObstetricos(),
                cdp = ControlDePeso(),
                telefono = "",
                direccion = "",
                ciudad = "",
                ocupacion = ""
            )
        )
    }

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val successMessage by viewModel.successMessage.collectAsStateWithLifecycle()

    val headerBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF006192), Color(0xFF5ABDEF))
    )
    val contentBackground = Color(0xFF5ABDEF)
    val fabGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF006192), Color(0xFF5ABDEF))
    )

    val onSave = {
        viewModel.crearPaciente(nuevoPaciente)
    }

    var mostrarDialogo by remember { mutableStateOf(false) }
    LaunchedEffect(successMessage, errorMessage) {
        if (successMessage != null || errorMessage != null) {
            mostrarDialogo = true
        }
    }

    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            navController.popBackStack()
            viewModel.limpiarMensajes()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSave,
                containerColor = Color(0xFF006192), // Color sólido para evitar el cuadro blanco
                contentColor = Color.White,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Guardar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.White)
        ) {
            // HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(brush = headerBackground, shape = RoundedCornerShape(bottomEnd = 70.dp)),
                contentAlignment = Alignment.Center
            ) {
                HeaderComponent(title = "Nuevo Paciente", subtitle = "Ficha de Registro")
            }

            // CUERPO
            Box(
                modifier = Modifier.background(color = contentBackground).fillMaxWidth().weight(4f)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(topStart = 50.dp))
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    if (isLoading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
                    }

                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                        item {
                            // Reutilizamos DetallesDeFicha pero en modo edición forzado
                            DetallesDeFicha(
                                paciente = nuevoPaciente,
                                isEditing = true,
                                onValueChange = { nuevoPaciente = it }
                            )
                            Spacer(modifier = Modifier.height(80.dp)) // Espacio para el FAB
                        }
                    }
                }
            }
        }
        if (mostrarDialogo) {
            AvisoDialog(
                titulo = if (errorMessage != null) "Atención" else "¡Éxito!",
                mensaje = errorMessage ?: successMessage ?: "",
                esError = errorMessage != null,
                onDismiss = {
                    mostrarDialogo = false
                    viewModel.limpiarMensajes() // Importante limpiar para que no se repita
                }
            )
        }
    }
}