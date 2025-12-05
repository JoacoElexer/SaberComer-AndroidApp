package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.PatientsViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: PatientsViewModel) {
    val pacientes by viewModel.pacientes.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val success by viewModel.successMessage.collectAsStateWithLifecycle()
    val headerBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF006192),
            Color(0xFF5ABDEF)
        )
    )
    val secondBackground = Color(0xFF5ABDEF)
    
    // Global container
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .background(brush = headerBackground, shape = RoundedCornerShape(
                    bottomEnd = 70.dp
                ))
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text("Hola")
        }
        // Content Background
        Box(
            modifier = Modifier
                .background(color = secondBackground)
                .fillMaxWidth()
                .weight(3f)
        )
        {
            // Content Container
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(
                        topStart = 50.dp
                    ))
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
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