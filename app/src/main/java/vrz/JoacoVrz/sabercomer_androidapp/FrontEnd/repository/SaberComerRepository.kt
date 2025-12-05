package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.repository

import android.util.Log
import vrz.JoacoVrz.sabercomer_androidapp.BackEnd.ApiService
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.ControlClinico
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.LoginRequest
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.Paciente
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.RecordMedidas
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.Usuario
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.utils.Resource
import java.io.IOException

class SaberComerRepository(private val api: ApiService) {
    // ===== LOGIN ===== //
    suspend fun login(correo: String, pin: String): Resource<Usuario> {
        return safeApiCall {
            api.loginUser(LoginRequest(correo, pin))
        }
    }

    suspend fun getUsuarioPorCorreo(correo: String): Resource<Usuario> {
        return safeApiCall { api.getUserByEmail(correo) }
    }

    // ===== PACIENTES ===== //
    suspend fun getPacientes(): Resource<List<Paciente>> {
        // En listas, si es null devolvemos lista vacía para evitar null pointers
        return safeApiCall(defaultIfNull = emptyList()) { api.getPacientes() }
    }

    suspend fun getPacientePorId(id: String): Resource<Paciente> {
        return safeApiCall { api.getPacienteById(id) }
    }

    suspend fun buscarPacientesPorNombre(nombre: String): Resource<List<Paciente>> {
        return safeApiCall(defaultIfNull = emptyList()) { api.getPacienteByName(nombre) }
    }

    suspend fun buscarPacientesPorTelefono(telefono: String): Resource<List<Paciente>> {
        return safeApiCall(defaultIfNull = emptyList()) { api.getPacienteByTelefono(telefono) }
    }

    suspend fun crearPaciente(paciente: Paciente): Resource<Paciente> {
        return safeApiCall { api.createPaciente(paciente) }
    }

    suspend fun actualizarPaciente(id: String, paciente: Paciente): Resource<Paciente> {
        return safeApiCall { api.updatePaciente(id, paciente) }
    }

    suspend fun borrarPaciente(id: String): Resource<Boolean> {
        return try {
            val response = api.deletePaciente(id)
            if (response.isSuccessful) Resource.Success(true)
            else Resource.Error("Error al eliminar: ${response.code()}")
        } catch (e: Exception) {
            handleException(e)
        }
    }

    // ===== CCONTROL CLINICO ===== //
    suspend fun getControlesDePaciente(pacienteId: String): Resource<List<ControlClinico>> {
        return safeApiCall(defaultIfNull = emptyList()) { api.getControlClinicoById(pacienteId) }
    }

    suspend fun crearControlClinico(pacienteId: String, control: ControlClinico): Resource<ControlClinico> {
        return safeApiCall { api.createControlClinico(pacienteId, control) }
    }

    suspend fun actualizarControlClinico(mongoId: String, control: ControlClinico): Resource<ControlClinico> {
        return safeApiCall { api.updateControlClinico(mongoId, control) }
    }

    suspend fun borrarControlClinico(mongoId: String): Resource<Boolean> {
        return try {
            val response = api.deleteControlClinico(mongoId)
            if (response.isSuccessful) Resource.Success(true)
            else Resource.Error("Error al eliminar control")
        } catch (e: Exception) {
            handleException(e)
        }
    }

    // ===== RECORD DE MEDIDAS ===== //
    suspend fun getMedidasDePaciente(pacienteId: String): Resource<List<RecordMedidas>> {
        return safeApiCall(defaultIfNull = emptyList()) { api.getRecordMedidasById(pacienteId) }
    }

    suspend fun crearRecordMedidas(pacienteId: String, medidas: RecordMedidas): Resource<RecordMedidas> {
        return safeApiCall { api.createRecordMedidas(pacienteId, medidas) }
    }

    suspend fun actualizarRecordMedidas(mongoId: String, medidas: RecordMedidas): Resource<RecordMedidas> {
        return safeApiCall { api.updateRecordMedidas(mongoId, medidas) }
    }

    suspend fun borrarRecordMedidas(mongoId: String): Resource<Boolean> {
        return try {
            val response = api.deleteRecordMedidas(mongoId)
            if (response.isSuccessful) Resource.Success(true)
            else Resource.Error("Error al eliminar medida")
        } catch (e: Exception) {
            handleException(e)
        }
    }

    // ===== HELPERS ===== //
    private suspend fun <T> safeApiCall(
        defaultIfNull: T? = null,
        apiCall: suspend () -> retrofit2.Response<T>
    ): Resource<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else if (defaultIfNull != null) {
                    // Si el body es null pero tenemos un valor por defecto (ej. lista vacía)
                    Resource.Success(defaultIfNull)
                } else {
                    Resource.Error("Respuesta vacía del servidor")
                }
            } else {
                Resource.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun <T> handleException(e: Exception): Resource<T> {
        Log.e("REPOSITORY", "API Error: ", e)
        return when (e) {
            is IOException -> Resource.Error("Error de conexión. Verifica tu internet.")
            else -> Resource.Error("Error inesperado: ${e.message}")
        }
    }
}