package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AvisoDialog(
    titulo: String,
    mensaje: String,
    esError: Boolean = false,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = if (esError) Icons.Default.Error else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (esError) Color.Red else Color(0xFF006192)
            )
        },
        title = {
            Text(text = titulo, style = MaterialTheme.typography.headlineSmall, color = Color.Black)
        },
        text = {
            Text(text = mensaje, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar", color = Color(0xFF006192), fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(28.dp),
        containerColor = Color.White
    )
}