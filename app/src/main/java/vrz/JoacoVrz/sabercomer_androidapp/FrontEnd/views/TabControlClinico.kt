package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components.AvisoDialog
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components.ConfirmacionDialog
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.ControlClinico
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.OpcionesControl
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.utils.DateUtils
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.ControlClinicoViewModel
import java.time.LocalDate
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabControlClinico(
    pacienteId: String,
    viewModel: ControlClinicoViewModel
) {
    val controles by viewModel.controles.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val successMessage by viewModel.successMessage.collectAsStateWithLifecycle()
    val mostrarSheet by viewModel.mostrarBottomSheet.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Control seleccionado para editar (null = nuevo)
    var controlEditando by remember { mutableStateOf<ControlClinico?>(null) }
    var controlAEliminar by remember { mutableStateOf<ControlClinico?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(successMessage, errorMessage) {
        if (successMessage != null || errorMessage != null) {
            mostrarDialogo = true
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (controles.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay controles registrados.", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                top = 12.dp, bottom = 80.dp
            )
        ) {
            items(controles) { control ->
                ControlClinicoCard(
                    control = control,
                    onEditClick = {
                        controlEditando = control
                        viewModel.mostrarBottomSheet()
                    },
                    onDeleteClick = { controlAEliminar = control }
                )
            }
        }
    }

    // Bottom Sheet — nuevo o editar
    if (mostrarSheet) {
        BackHandler(enabled = true){focusManager.clearFocus()}
        ModalBottomSheet(
            onDismissRequest = {
                focusManager.clearFocus()
                viewModel.ocultarBottomSheet()
                controlEditando = null
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            ControlClinicoForm(
                pacienteId = pacienteId,
                controlExistente = controlEditando,
                onGuardar = { control ->
                    if (controlEditando != null) {
                        viewModel.actualizarControl(control)
                    } else {
                        viewModel.crearControl(control)
                    }
                    viewModel.ocultarBottomSheet()
                    controlEditando = null
                },
                onCancelar = {
                    viewModel.ocultarBottomSheet()
                    controlEditando = null
                }
            )
        }
    }

    // Diálogo de confirmación de eliminación
    if (controlAEliminar != null) {
        ConfirmacionDialog(
            titulo = "Eliminar control",
            mensaje = "¿Eliminar el control del ${DateUtils.formatearFecha(controlAEliminar!!.fecha)}?",
            onConfirmar = {
                viewModel.borrarControl(controlAEliminar!!.mongoId)
                controlAEliminar = null
            },
            onCancelar = { controlAEliminar = null }
        )
    }

    // Diálogo de aviso
    if (mostrarDialogo) {
        AvisoDialog(
            titulo = if (errorMessage != null) "Atención" else "¡Éxito!",
            mensaje = errorMessage ?: successMessage ?: "",
            esError = errorMessage != null,
            onDismiss = {
                mostrarDialogo = false
                viewModel.limpiarMensajes()
            }
        )
    }
}

@Composable
private fun ControlClinicoCard(
    control: ControlClinico,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val primaryColor = Color(0xFF006192)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado: fecha + botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = DateUtils.formatearFecha(control.fecha),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar",
                            tint = primaryColor)
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar",
                            tint = Color.Red)
                    }
                }
            }

            Divider(color = Color.LightGray, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Peso y calificación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (control.peso != null) {
                    Text("⚖️ ${control.peso} kg", fontSize = 14.sp, color = Color.Black)
                }
                if (control.calificacion != null) {
                    Text("⭐ ${control.calificacion}/10", fontSize = 14.sp, color = Color.Black)
                }
            }

            // Guía y TX
            if (!control.guia.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Guía: ${control.guia}", fontSize = 13.sp, color = Color.DarkGray)
            }
            if (!control.tx.isNullOrBlank()) {
                Text("TX: ${control.tx}", fontSize = 13.sp, color = Color.DarkGray)
            }

            // Opciones activas
            val opciones = control.opcionesControl
            if (opciones != null) {
                val activas = buildList {
                    if (opciones.mesoterapia) add("Mesoterapia")
                    if (opciones.acupuntura) add("Acupuntura")
                    if (opciones.ejercicios) add("Ejercicios")
                    if (opciones.agua) add("Agua")
                }.joinToString(" · ")

                if (activas.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(activas, fontSize = 12.sp, color = primaryColor)
                }
            }

            // Observaciones
            if (!control.observaciones.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "\"${control.observaciones}\"",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
private fun ControlClinicoForm(
    pacienteId: String,
    controlExistente: ControlClinico?,
    onGuardar: (ControlClinico) -> Unit,
    onCancelar: () -> Unit
) {
    val primaryColor = Color(0xFF006192)
    val today = LocalDate.now().toString()

    var fecha by remember {
        mutableStateOf(controlExistente?.fecha?.substring(0, 10) ?: today)
    }
    var peso by remember {
        mutableStateOf(controlExistente?.peso?.toString() ?: "")
    }
    var guia by remember { mutableStateOf(controlExistente?.guia ?: "") }
    var tx by remember { mutableStateOf(controlExistente?.tx ?: "") }
    var observaciones by remember { mutableStateOf(controlExistente?.observaciones ?: "") }
    var calificacion by remember {
        mutableStateOf(controlExistente?.calificacion?.toFloat() ?: 5f)
    }
    var mesoterapia by remember {
        mutableStateOf(controlExistente?.opcionesControl?.mesoterapia ?: false)
    }
    var acupuntura by remember {
        mutableStateOf(controlExistente?.opcionesControl?.acupuntura ?: false)
    }
    var ejercicios by remember {
        mutableStateOf(controlExistente?.opcionesControl?.ejercicios ?: false)
    }
    var agua by remember {
        mutableStateOf(controlExistente?.opcionesControl?.agua ?: false)
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = primaryColor,
        unfocusedBorderColor = Color.LightGray,
        focusedLabelColor = primaryColor,
        unfocusedLabelColor = Color.Gray,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )

    val switchColors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = primaryColor,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = Color.LightGray
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 32.dp)
    ) {
        item {
            Text(
                text = if (controlExistente != null) "Editar Control" else "Nuevo Control",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (yyyy-MM-dd)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors
            )
        }
        item {
            OutlinedTextField(
                value = peso,
                onValueChange = { peso = it },
                label = { Text("Peso (kg)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                colors = textFieldColors
            )
        }
        item {
            OutlinedTextField(
                value = guia,
                onValueChange = { guia = it },
                label = { Text("Guía") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors
            )
        }
        item {
            OutlinedTextField(
                value = tx,
                onValueChange = { tx = it },
                label = { Text("TX") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors
            )
        }
        item {
            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Observaciones") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 3,
                colors = textFieldColors
            )
        }
        item {
            Text(
                "Calificación: ${calificacion.toInt()}/10",
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
            Slider(
                value = calificacion,
                onValueChange = { calificacion = it },
                valueRange = 1f..10f,
                steps = 8,
                colors = SliderDefaults.colors(
                    thumbColor = primaryColor,
                    activeTrackColor = primaryColor
                )
            )
        }
        item {
            Divider()
            Text(
                "Opciones de tratamiento",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            SwitchFila("Mesoterapia", mesoterapia, switchColors) { mesoterapia = it }
            SwitchFila("Acupuntura", acupuntura, switchColors) { acupuntura = it }
            SwitchFila("Ejercicios", ejercicios, switchColors) { ejercicios = it }
            SwitchFila("Agua", agua, switchColors) { agua = it }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar", color = Color.Gray)
                }
                androidx.compose.material3.Button(
                    onClick = {
                        onGuardar(
                            ControlClinico(
                                mongoId = controlExistente?.mongoId ?: "",
                                id = pacienteId,
                                fecha = fecha,
                                peso = peso.toDoubleOrNull(),
                                guia = guia,
                                tx = tx,
                                observaciones = observaciones,
                                calificacion = calificacion.toInt(),
                                opcionesControl = OpcionesControl(
                                    mesoterapia = mesoterapia,
                                    acupuntura = acupuntura,
                                    ejercicios = ejercicios,
                                    agua = agua
                                )
                            )
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = primaryColor
                    )
                ) {
                    Text("Guardar", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun SwitchFila(
    label: String,
    checked: Boolean,
    colors: androidx.compose.material3.SwitchColors,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Black)
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = colors)
    }
}