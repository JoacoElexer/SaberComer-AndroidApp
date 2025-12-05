package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components.InputComponent
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.components.PwInputComponent
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels.LoginViewModel
import vrz.JoacoVrz.sabercomer_androidapp.R

@Composable
fun LoginScreen(viewModel: LoginViewModel, onLoginSuccess: () -> Unit){
    val state by viewModel.loginState.collectAsStateWithLifecycle()
    var correo by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") } // TODO: eliminar valores por defecto

    LaunchedEffect(state) {
        if(state is LoginViewModel.LoginState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(
            painter = painterResource(R.drawable.background_img_top),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ){
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(
                                topStart = 35.dp,
                                topEnd = 0.dp,
                                bottomStart = 35.dp,
                                bottomEnd = 35.dp
                            )
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        painter = painterResource(R.drawable.isologotipo_color_sin_letras),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
            Box (
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(
                            topStart = 60.dp,
                            topEnd = 0.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    ),
                contentAlignment = Alignment.Center
            ){
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        "Login",
                        fontSize = 40.sp
                    )
                    Spacer(Modifier.height(50.dp))
                    InputComponent("Email", "Example17@gmail.com", correo, onValueChange = { correo = it })
                    Spacer(modifier = Modifier
                        .height(30.dp))
                    PwInputComponent("Password", pin, onValueChange = { pin = it })
                    Spacer(modifier = Modifier
                        .height(30.dp))
                    if (state is LoginViewModel.LoginState.Error) {
                        Text(
                            (state as LoginViewModel.LoginState.Error).mensaje,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    Button(
                        onClick = { viewModel.login(correo, pin) },
                        modifier = Modifier
                            .width(300.dp)
                            .clip(RoundedCornerShape(50.dp)),
                        enabled = state !is LoginViewModel.LoginState.Loading
                    ) {
                        if(state is LoginViewModel.LoginState.Loading) {
                            CircularProgressIndicator(
                                color = Color.Black,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Login"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row {
                        Text(
                            "Don't have an account?"
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = "sign up",
                            color = Color.Blue,
                            modifier = Modifier
                                .clickable(
                                    onClick = {} //{ navController.navigate("RegisterScreenRoute") }
                                ) // TODO: implementar registro
                        )
                    }
                }
            }
        }
    }
}