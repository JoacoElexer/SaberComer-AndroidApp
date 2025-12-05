package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PatientDetailScreen() {


    val headerBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF006192),
            Color(0xFF5ABDEF)
        )
    )
    val secondBackground = Color(0xFF5ABDEF)

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
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
        Box(
            modifier = Modifier
                .background(color = secondBackground)
                .fillMaxWidth()
                .weight(3f)
        )
        {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(
                        topStart = 50.dp
                    ))
                    .fillMaxSize()
            ) { }
        }
    }
}