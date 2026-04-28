package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.MedidasCorporales
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.RecordMedidas
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.utils.DateUtils
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.RecordMedidasViewModel
import java.time.LocalDate
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRecordMedidas(
    pacienteId: String,
    viewModel: RecordMedidasViewModel
) {
    val medidasList by viewModel.medidasList.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val successMessage by viewModel.successMessage.collectAsStateWithLifecycle()
    val mostrarSheet by viewModel.mostrarBottomSheet.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var medidaEditando by remember { mutableStateOf<RecordMedidas?>(null) }
    var medidaAEliminar by remember { mutableStateOf<RecordMedidas?>(null) }
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

    if (medidasList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay medidas registradas.", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
        ) {
            items(medidasList) { medida ->
                RecordMedidasCard(
                    medida = medida,
                    onEditClick = {
                        medidaEditando = medida
                        viewModel.mostrarBottomSheet()
                    },
                    onDeleteClick = { medidaAEliminar = medida }
                )
            }
        }
    }

    if (mostrarSheet) {
        BackHandler(enabled = true){focusManager.clearFocus()}
        ModalBottomSheet(
            onDismissRequest = {
                focusManager.clearFocus()
                viewModel.ocultarBottomSheet()
                medidaEditando = null
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            RecordMedidasForm(
                pacienteId = pacienteId,
                medidaExistente = medidaEditando,
                onGuardar = { medida ->
                    if (medidaEditando != null) {
                        viewModel.actualizarMedida(medida)
                    } else {
                        viewModel.crearMedida(medida)
                    }
                    viewModel.ocultarBottomSheet()
                    medidaEditando = null
                },
                onCancelar = {
                    viewModel.ocultarBottomSheet()
                    medidaEditando = null
                }
            )
        }
    }

    if (medidaAEliminar != null) {
        ConfirmacionDialog(
            titulo = "Eliminar medida",
            mensaje = "¿Eliminar el registro del ${DateUtils.formatearFecha(medidaAEliminar!!.fecha)}?",
            onConfirmar = {
                viewModel.borrarMedida(medidaAEliminar!!.mongoId)
                medidaAEliminar = null
            },
            onCancelar = { medidaAEliminar = null }
        )
    }

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
private fun RecordMedidasCard(
    medida: RecordMedidas,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = DateUtils.formatearFecha(medida.fecha),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = primaryColor)
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                }
            }

            Divider(color = Color.LightGray, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))

            val m = medida.medidas
            if (m != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MedidaItem("Busto", m.busto)
                    MedidaItem("Abd. Alto", m.abdomenAlto)
                    MedidaItem("Ombligo", m.ombligo)
                    MedidaItem("Cadera", m.cadera)
                }
            }

            if (!medida.observaciones.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "\"${medida.observaciones}\"",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
private fun MedidaItem(label: String, valor: Double?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = valor?.let { "$it" } ?: "-",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(text = label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
private fun RecordMedidasForm(
    pacienteId: String,
    medidaExistente: RecordMedidas?,
    onGuardar: (RecordMedidas) -> Unit,
    onCancelar: () -> Unit
) {
    val primaryColor = Color(0xFF006192)
    val today = LocalDate.now().toString()

    var fecha by remember {
        mutableStateOf(medidaExistente?.fecha?.substring(0, 10) ?: today)
    }
    var busto by remember { mutableStateOf(medidaExistente?.medidas?.busto?.toString() ?: "") }
    var abdomenAlto by remember {
        mutableStateOf(medidaExistente?.medidas?.abdomenAlto?.toString() ?: "")
    }
    var ombligo by remember {
        mutableStateOf(medidaExistente?.medidas?.ombligo?.toString() ?: "")
    }
    var cadera by remember { mutableStateOf(medidaExistente?.medidas?.cadera?.toString() ?: "") }
    var observaciones by remember { mutableStateOf(medidaExistente?.observaciones ?: "") }

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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = if (medidaExistente != null) "Editar Medidas" else "Nuevas Medidas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha (yyyy-MM-dd)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )
        // Las 4 medidas en dos filas de 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = busto,
                onValueChange = { busto = it },
                label = { Text("Busto (cm)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                colors = textFieldColors
            )
            OutlinedTextField(
                value = abdomenAlto,
                onValueChange = { abdomenAlto = it },
                label = { Text("Abd. Alto (cm)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                colors = textFieldColors
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = ombligo,
                onValueChange = { ombligo = it },
                label = { Text("Ombligo (cm)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                colors = textFieldColors
            )
            OutlinedTextField(
                value = cadera,
                onValueChange = { cadera = it },
                label = { Text("Cadera (cm)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                colors = textFieldColors
            )
        }
        OutlinedTextField(
            value = observaciones,
            onValueChange = { observaciones = it },
            label = { Text("Observaciones") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 3,
            colors = textFieldColors
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onCancelar, modifier = Modifier.weight(1f)) {
                Text("Cancelar", color = Color.Gray)
            }
            Button(
                onClick = {
                    onGuardar(
                        RecordMedidas(
                            mongoId = medidaExistente?.mongoId ?: "",
                            id = pacienteId,
                            fecha = fecha,
                            medidas = MedidasCorporales(
                                busto = busto.toDoubleOrNull(),
                                abdomenAlto = abdomenAlto.toDoubleOrNull(),
                                ombligo = ombligo.toDoubleOrNull(),
                                cadera = cadera.toDoubleOrNull()
                            ),
                            observaciones = observaciones
                        )
                    )
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}