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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CrearPacienteScreen(navController: NavController, viewModel: PatientsViewModel) {

    val todayDate = remember {
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    var nuevoPaciente by remember {
        mutableStateOf(
            Paciente(
                id = UUID.randomUUID().toString(),
                nombre = "",
                fechaNacimiento = "",
                fechaInicio = todayDate,
                controlDePeso = ControlDePeso(),
                antecedentesGinecoObstetricos = AntecedentesGinecoObstetricos(),
                telefono = ""
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

    val onSave = fun() {
        if (nuevoPaciente.nombre.isBlank() || nuevoPaciente.fechaNacimiento.isBlank()) {
            println("Error: Nombre y Fecha de Nacimiento son obligatorios.")
            return
        }

        viewModel.crearPaciente(nuevoPaciente)
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
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    modifier = Modifier.background(
                        brush = fabGradient,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Guardar"
                    )
                }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.White)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(brush = headerBackground, shape = RoundedCornerShape(bottomEnd = 70.dp)),
                contentAlignment = Alignment.Center
            ) {
                HeaderComponent(
                    title = "Nuevo Paciente",
                    subtitle = "Ficha de Registro"
                )
            }

            Box(
                modifier = Modifier
                    .background(color = contentBackground)
                    .fillMaxWidth()
                    .weight(4f)
            )
            {
                Column(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(topStart = 50.dp))
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {

                    errorMessage?.let { Text(it, color = Color.Red, modifier = Modifier.padding(16.dp)) }

                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {

                        item {
                            Text("I. Datos Generales", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.Black)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            // Nombre (Obligatorio)
                            InputText(label = "Nombre *", value = nuevoPaciente.nombre) {
                                nuevoPaciente = nuevoPaciente.copy(nombre = it)
                            }
                            // Teléfono
                            InputText(label = "Teléfono", value = nuevoPaciente.telefono.orEmpty(), keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone) {
                                nuevoPaciente = nuevoPaciente.copy(telefono = it)
                            }
                            // Fecha Nacimiento (Obligatorio)
                            InputText(label = "Fecha Nacimiento (AAAA-MM-DD) *", value = nuevoPaciente.fechaNacimiento) {
                                nuevoPaciente = nuevoPaciente.copy(fechaNacimiento = it)
                            }
                            // Ocupación
                            InputText(label = "Ocupación", value = nuevoPaciente.ocupacion.orEmpty()) {
                                nuevoPaciente = nuevoPaciente.copy(ocupacion = it)
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text("II. Antecedentes Heredofamiliares", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.Black)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            SwitchCampo(label = "Hipertensión (HTA)", checked = nuevoPaciente.hta) {
                                nuevoPaciente = nuevoPaciente.copy(hta = it)
                            }
                            SwitchCampo(label = "Diabetes (DM)", checked = nuevoPaciente.dm) {
                                nuevoPaciente = nuevoPaciente.copy(dm = it)
                            }
                            InputText(label = "Otros AHFs", value = nuevoPaciente.ahfOtros.orEmpty()) {
                                nuevoPaciente = nuevoPaciente.copy(ahfOtros = it)
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text("III. Antecedentes Gineco-Obstétricos", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.Black)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            InputText(label = "Gestaciones (G)", value = nuevoPaciente.antecedentesGinecoObstetricos?.g.orEmpty()) {
                                nuevoPaciente = nuevoPaciente.copy(
                                    antecedentesGinecoObstetricos = nuevoPaciente.antecedentesGinecoObstetricos?.copy(g = it)
                                )
                            }
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InputText(label: String, value: String, keyboardType: KeyboardType = KeyboardType.Text, onValueChange: (String) -> Unit) {
    val primaryColor = Color(0xFF006192)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.LightGray.copy(alpha = 0.3f),

            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,

            focusedLabelColor = primaryColor,
            unfocusedLabelColor = Color.Gray,

            focusedBorderColor = primaryColor,
            unfocusedBorderColor = Color.LightGray
        )
    )
}

@Composable
fun SwitchCampo(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}