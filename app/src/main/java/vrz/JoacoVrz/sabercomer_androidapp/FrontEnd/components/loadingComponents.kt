package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Un indicador de carga que bloquea la pantalla completa.
 * Úsalo cuando la operación es bloqueante (ej. Login, Guardar datos).
 */
@Composable
fun FullScreenLoader(isLoading: Boolean) {
    if (isLoading) {
        Dialog(
            onDismissRequest = { /* No permitir cerrar al tocar fuera */ },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .fillMaxSize(fraction = 0.2f) // Un pequeño cuadro blanco
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

/**
 * Un indicador de carga que se superpone al contenido sin bloquearlo completamente (opcional).
 * Úsalo dentro de un Box en pantallas de listas.
 */
@Composable
fun LoadingOverlay(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)), // Fondo semitransparente oscuro
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}