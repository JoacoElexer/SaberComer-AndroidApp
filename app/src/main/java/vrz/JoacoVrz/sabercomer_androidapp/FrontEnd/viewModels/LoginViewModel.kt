package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.Usuario
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.repository.SaberComerRepository
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.utils.Resource

class LoginViewModel(private val repository: SaberComerRepository) : ViewModel() {
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val usuario: Usuario) : LoginState()
        data class Error(val mensaje: String) : LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(correo: String, pin: String){
        if (correo.isBlank() || pin.isBlank()) {
            _loginState.value = LoginState.Error("Por favor completa todos los campos")
            return
        }
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            when(val result = repository.login(correo, pin)) {
                is Resource.Success -> {
                    if (result.data != null) {
                        _loginState.value = LoginState.Success(result.data)
                    } else {
                        _loginState.value = LoginState.Error("Error: Datos de usuario vacíos")
                    }
                }
                is Resource.Error -> {
                    _loginState.value = LoginState.Error(result.message ?: "Error desconocido")
                } else -> {
                    _loginState.value = LoginState.Error(result.message ?: "Error desconocido")
                }
            }
        }
    }

    // Función para resetear el estado (ej. al salir de la app)
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}