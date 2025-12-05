package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputComponent(text: String, placeholder: String, t: String, onValueChange: (String) -> Unit){
    Box (
        modifier = Modifier
            .width(300.dp)
            .height(80.dp)
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            text = text,
            modifier = Modifier
                .padding(bottom = 40.dp, top = 10.dp)
        )
        BasicTextField(
            value = t,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            decorationBox = { innerTextField ->
                if (t.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.LightGray,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun PwInputComponent(text: String, t: String, onValueChange: (String) -> Unit){
    Box (
        modifier = Modifier
            .width(300.dp)
            .height(80.dp)
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            text = text,
            modifier = Modifier
                .padding(bottom = 40.dp, top = 10.dp)
        )
        BasicTextField(
            value = t,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            visualTransformation = PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                if (t.isEmpty()) {
                    Text(
                        text = t,
                        color = Color.LightGray,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        )
    }
}