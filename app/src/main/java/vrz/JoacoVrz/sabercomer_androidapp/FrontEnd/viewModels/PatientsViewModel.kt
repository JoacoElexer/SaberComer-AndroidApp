package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.Paciente
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.repository.SaberComerRepository
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.utils.Resource

class PatientsViewModel(private val repository: SaberComerRepository) : ViewModel() {
    // Lista principal
    private val _pacientes = MutableStateFlow<List<Paciente>>(emptyList())
    val pacientes: StateFlow<List<Paciente>> = _pacientes

    // Paciente seleccionado (para ver detalle o editar)
    private val _pacienteSeleccionado = MutableStateFlow<Paciente?>(null)
    val pacienteSeleccionado: StateFlow<Paciente?> = _pacienteSeleccionado

    // Estados de UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    init {
        cargarPacientes()
    }

    fun cargarPacientes() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getPacientes()) {
                is Resource.Success -> _pacientes.value = result.data ?: emptyList()
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun buscarPaciente(nombre: String) {
        if (nombre.isBlank()) {
            cargarPacientes()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.buscarPacientesPorNombre(nombre)) {
                is Resource.Success -> _pacientes.value = result.data ?: emptyList()
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun cargarDetallePAciente(id: String) {
        if (id.isBlank()) {
            _pacienteSeleccionado.value = null
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getPacientePorId(id)) {
                is Resource.Success -> _pacienteSeleccionado.value = result.data
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
        }
    }

    fun seleccionarPaciente(paciente: Paciente) {
        _pacienteSeleccionado.value = paciente
    }

    fun crearPaciente(paciente: Paciente) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.crearPaciente(paciente)) {
                is Resource.Success -> {
                    _successMessage.value = "Paciente registrado correctamente"
                    cargarPacientes() // Recargar lista
                }
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun actualizarPaciente(paciente: Paciente) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.actualizarPaciente(paciente.id, paciente)) {
                is Resource.Success -> {
                    _successMessage.value = "Paciente actualizado"
                    cargarPacientes()
                }
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun borrarPaciente(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.borrarPaciente(id)) {
                is Resource.Success -> {
                    _successMessage.value = "Paciente eliminado"
                    cargarPacientes()
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