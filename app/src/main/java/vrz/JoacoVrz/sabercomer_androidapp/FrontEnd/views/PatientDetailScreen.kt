package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components.HeaderComponent
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.Paciente
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.PatientsViewModel

@Composable
fun PatientDetailScreen(navController: NavController, pacienteId: String, viewModel: PatientsViewModel) {

    val pacienteDetalle by viewModel.pacienteSeleccionado.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val successMessage by viewModel.successMessage.collectAsStateWithLifecycle()

    var isEditing by remember { mutableStateOf(false) }

    var editablePaciente by remember(pacienteDetalle) {
        mutableStateOf(pacienteDetalle?.copy())
    }

    val headerBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF006192), Color(0xFF5ABDEF))
    )
    val contentBackground = Color(0xFF5ABDEF)
    val fabGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF006192), Color(0xFF5ABDEF))
    )

    LaunchedEffect(pacienteId) {
        viewModel.cargarDetallePAciente(pacienteId)
    }

    LaunchedEffect(successMessage) {
        if (successMessage != null && isEditing) {
            isEditing = false
            viewModel.limpiarMensajes()
        }
    }

    val onSave = {
        editablePaciente?.let { p ->
            viewModel.actualizarPaciente(p)
        }
    }

    Scaffold(
        floatingActionButton = {
            if (pacienteDetalle != null) {
                FloatingActionButton(
                    onClick = {
                        if (isEditing) onSave() else isEditing = true
                    },
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    modifier = Modifier.background(
                        brush = fabGradient,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Filled.Add else Icons.Filled.Edit,
                        contentDescription = if (isEditing) "Guardar" else "Editar"
                    )
                }
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
                    title = pacienteDetalle?.nombre ?: "Detalle Paciente",
                    subtitle = if (isEditing) "Modo Edición" else "Ficha Clínica"
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

                    if (isLoading && pacienteDetalle == null) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    errorMessage?.let { Text(it, color = Color.Red, modifier = Modifier.padding(16.dp)) }

                    pacienteDetalle?.let {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                            item {
                                DetallesDeFicha(
                                    paciente = editablePaciente ?: it,
                                    isEditing = isEditing,
                                    onValueChange = { updated -> editablePaciente = updated }
                                )
                            }
                        }
                    } ?: run {
                        if (!isLoading) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Paciente no encontrado.")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetallesDeFicha(paciente: Paciente, isEditing: Boolean, onValueChange: (Paciente) -> Unit) {

    @Composable
    fun Campo(label: String, value: String, keyboardType: KeyboardType = KeyboardType.Text, setter: (String) -> Unit) {
        if (isEditing) {
            InputText(
                label = label,
                value = value,
                keyboardType = keyboardType,
                onValueChange = setter
            )
        } else {
            CampoSoloLectura(label = label, value = value)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text("Información Básica", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.Black)
        Divider(modifier = Modifier.padding(vertical = 4.dp))

        // --- CAMPOS EDITABLES ---
        Campo(
            label = "Nombre",
            value = paciente.nombre,
            setter = { onValueChange(paciente.copy(nombre = it)) }
        )
        Campo(
            label = "Teléfono",
            value = paciente.telefono.orEmpty(),
            keyboardType = KeyboardType.Phone,
            setter = { onValueChange(paciente.copy(telefono = it)) }
        )
        Campo(
            label = "Ocupación",
            value = paciente.ocupacion.orEmpty(),
            setter = { onValueChange(paciente.copy(ocupacion = it)) }
        )

        // Campo de Fechas (Solo lectura para esta demo, pero se puede editar)
        CampoSoloLectura(label = "Fecha Nacimiento", value = paciente.fechaNacimiento)
        CampoSoloLectura(label = "Fecha de Inicio", value = paciente.fechaInicio)

        Spacer(modifier = Modifier.height(20.dp))
        Text("Antecedentes Heredofamiliares", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.Black)
        Divider(modifier = Modifier.padding(vertical = 4.dp))

        // --- SWITCHES DE BOOLEANOS ---
        SwitchCampo(
            label = "Hipertensión (HTA)",
            checked = paciente.hta,
            enabled = isEditing,
            onCheckedChange = { onValueChange(paciente.copy(hta = it)) }
        )
        SwitchCampo(
            label = "Diabetes (DM)",
            checked = paciente.dm,
            enabled = isEditing,
            onCheckedChange = { onValueChange(paciente.copy(dm = it)) }
        )

        Campo(
            label = "Otros AHFs",
            value = paciente.ahfOtros.orEmpty(),
            setter = { onValueChange(paciente.copy(ahfOtros = it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text("Control de Peso", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.Black)
        Divider(modifier = Modifier.padding(vertical = 4.dp))

        // --- SUBDOCUMENTO CONTROL DE PESO ---
        val peso = paciente.controlDePeso
        Campo(
            label = "Estatura (cm)",
            value = peso?.estatura?.toString().orEmpty(),
            keyboardType = KeyboardType.Number,
            setter = { onValueChange(paciente.copy(controlDePeso = peso?.copy(estatura = it.toDoubleOrNull()))) }
        )
        Campo(
            label = "Peso Ideal (kg)",
            value = peso?.pesoIdeal?.toString().orEmpty(),
            keyboardType = KeyboardType.Number,
            setter = { onValueChange(paciente.copy(controlDePeso = peso?.copy(pesoIdeal = it.toDoubleOrNull()))) }
        )
    }
}

@Composable
fun InputText(label: String, value: String, keyboardType: KeyboardType = KeyboardType.Text, enabled: Boolean = true, onValueChange: (String) -> Unit) {

    val primaryColor = Color(0xFF006192)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        singleLine = true,
        enabled = enabled,
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
        // -----------------------------------------------------------------
    )
}

@Composable
fun SwitchCampo(label: String, checked: Boolean, enabled: Boolean = true, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Composable
fun CampoSoloLectura(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = Color.Black)
    }
}