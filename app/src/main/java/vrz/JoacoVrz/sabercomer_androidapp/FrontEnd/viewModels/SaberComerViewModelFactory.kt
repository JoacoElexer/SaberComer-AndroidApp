package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.repository.SaberComerRepository

class SaberComerViewModelFactory(private val repository: SaberComerRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(PatientsViewModel::class.java)) {
            return PatientsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ControlClinicoViewModel::class.java)) {
            return ControlClinicoViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RecordMedidasViewModel::class.java)) {
            return RecordMedidasViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}