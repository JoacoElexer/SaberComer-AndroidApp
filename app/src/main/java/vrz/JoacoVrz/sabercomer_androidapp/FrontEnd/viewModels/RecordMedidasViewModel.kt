package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.RecordMedidas
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.repository.SaberComerRepository
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.utils.Resource

class RecordMedidasViewModel(private val repository: SaberComerRepository) : ViewModel() {
    private val _medidasList = MutableStateFlow<List<RecordMedidas>>(emptyList())
    val medidasList: StateFlow<List<RecordMedidas>> = _medidasList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private var currentPacienteId: String? = null

    fun cargarMedidas(pacienteId: String) {
        currentPacienteId = pacienteId
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getMedidasDePaciente(pacienteId)) {
                is Resource.Success -> _medidasList.value = result.data ?: emptyList()
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun crearMedida(medidas: RecordMedidas) {
        val pId = currentPacienteId ?: medidas.id
        if (pId.isBlank()) {
            _errorMessage.value = "Error: No hay paciente seleccionado"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.crearRecordMedidas(pId, medidas)) {
                is Resource.Success -> {
                    _successMessage.value = "Medidas registradas"
                    cargarMedidas(pId)
                }
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun actualizarMedida(medidas: RecordMedidas) {
        viewModelScope.launch {
            _isLoading.value = true
            // Usamos mongoId para el update
            when (val result = repository.actualizarRecordMedidas(medidas.mongoId, medidas)) {
                is Resource.Success -> {
                    _successMessage.value = "Medidas actualizadas"
                    currentPacienteId?.let { cargarMedidas(it) }
                }
                is Resource.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun borrarMedida(mongoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Usamos mongoId para el delete
            when (val result = repository.borrarRecordMedidas(mongoId)) {
                is Resource.Success -> {
                    _successMessage.value = "Registro eliminado"
                    currentPacienteId?.let { cargarMedidas(it) }
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