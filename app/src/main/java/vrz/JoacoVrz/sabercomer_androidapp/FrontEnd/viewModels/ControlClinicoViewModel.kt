package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.ControlClinico
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.repository.SaberComerRepository
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class ControlClinicoViewModel(private val repository: SaberComerRepository) : ViewModel() {
    private val _controles = MutableStateFlow<List<ControlClinico>>(emptyList())
    val controles: StateFlow<List<ControlClinico>> = _controles

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    // ID del paciente cargado actualmente
    private var currentPacienteId: String? = null

    // Cargar historial de un paciente específico
    fun cargarControles(pacienteId: String) {
        currentPacienteId = pacienteId
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getControlesDePaciente(pacienteId)) {
                is Resource.Success -> _controles.value = result.data ?: emptyList()
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun crearControl(control: ControlClinico) {
        val pId = currentPacienteId ?: control.id
        if (pId.isBlank()) {
            _errorMessage.value = "Error: No hay paciente seleccionado"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.crearControlClinico(pId, control)) {
                is Resource.Success -> {
                    _successMessage.value = "Control registrado"
                    cargarControles(pId) // Recargar lista
                }
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun actualizarControl(control: ControlClinico) {
        viewModelScope.launch {
            _isLoading.value = true
            // Importante: Usamos mongoId (_id) para actualizar
            when (val result = repository.actualizarControlClinico(control.mongoId, control)) {
                is Resource.Success -> {
                    _successMessage.value = "Control actualizado"
                    currentPacienteId?.let { cargarControles(it) }
                }
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun borrarControl(mongoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Importante: Usamos mongoId (_id) para borrar
            when (val result = repository.borrarControlClinico(mongoId)) {
                is Resource.Success -> {
                    _successMessage.value = "Control eliminado"
                    currentPacienteId?.let { cargarControles(it) }
                }
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun limpiarMensajes() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}