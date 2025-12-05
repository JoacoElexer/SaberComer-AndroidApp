package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vrz.JoacoVrz.sabercomer_androidapp.R

@Composable
fun HeaderComponent(title: String, subtitle: String) {
    val title = title
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 28.sp,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Transparent)
            Image(
                painter = painterResource(id = R.drawable.user_circle_fill),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)

@Composable
fun HeaderComponentPreview(){
    HeaderComponent("Title", "Subtitle")
}