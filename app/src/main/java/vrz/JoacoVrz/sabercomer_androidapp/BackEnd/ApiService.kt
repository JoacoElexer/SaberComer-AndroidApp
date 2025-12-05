package vrz.JoacoVrz.sabercomer_androidapp.BackEnd

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.ControlClinico
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.LoginRequest
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.Paciente
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.RecordMedidas
import vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.models.Usuario

interface ApiService {
    // ===== USUARIOS ===== //
    // Obtener usuario por correo
    @GET("users/email/{correo}")
    suspend fun getUserByEmail(@Path("correo") correo: String): Response<Usuario>
    // Obtener usuarios por rol
    @GET("users/role/{rol}")
    suspend fun getUserByRole(@Path("rol") rol: String): Response<List<Usuario>>
    // Crear usuario
    @POST("users/")
    suspend fun createUser(@Body usuario: Usuario): Response<Usuario>
    // Login
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<Usuario>
    // Actualizar usuario
    @PATCH("users/{id}")
    suspend fun updateUser(@Path("id") mongoId: String, @Body usuario: Usuario): Response<Usuario>
    // Eliminar usuario
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") mongoId: String): Response<Void>

    // ===== PACIENTES ===== //
    // Obtener todos los pacientes
    @GET("fichaPacientes")
    suspend fun getPacientes(): Response<List<Paciente>>
    // Obtener paciente por ID
    @GET("fichaPacientes/id/{id}")
    suspend fun getPacienteById(@Path("id") id: String): Response<Paciente>
    // Obtener paciente por nombre
    @GET("fichaPacientes/name/{nombre}")
    suspend fun getPacienteByName(@Path("nombre") nombre: String): Response<List<Paciente>>
    // Obtener paciente por teléfono
    @GET("fichaPacientes/tel/{telefono}")
    suspend fun getPacienteByTelefono(@Path("telefono") telefono: String): Response<List<Paciente>>
    // Obtener paciente por fecha de inicio de tratamiento
    @GET("fichaPacientes/startdate/{fechaInicio}")
    suspend fun getPacienteByFechaInicio(@Path("fechaInicio") fechaInicio: String): Response<List<Paciente>>
    // Crear paciente
    @POST("fichaPacientes/")
    suspend fun createPaciente(@Body paciente: Paciente): Response<Paciente>
    // Actualizar paciente
    @PATCH("fichaPacientes/{id}")
    suspend fun updatePaciente(@Path("id") id: String, @Body paciente: Paciente): Response<Paciente>
    // Eliminar paciente
    @DELETE("fichaPacientes/{id}")
    suspend fun deletePaciente(@Path("id") id: String): Response<Void>

    // ===== CONTROL CLINICO ===== //
    // Obtener todos los controles clínicos
    @GET("controlClinico")
    suspend fun getControlesClinicos(): Response<List<ControlClinico>>
    // Obtener una lista de controles clínicos por ID
    @GET("controlClinico/{id}")
    suspend fun getControlClinicoById(@Path("id") id: String): Response<List<ControlClinico>>
    // Obtiene una lista de controles clínicos por fecha
    @GET("controlClinico/date/{fecha}")
    suspend fun getControlClinicoByDate(@Path("fecha") fecha: String): Response<List<ControlClinico>>
    // Obtiene una lista de controles clínicos por calificación
    @GET("controlClinico/rating/{calificacion}")
    suspend fun getControlClinicoByRating(@Path("calificacion") calificacion: Int): Response<List<ControlClinico>>
    // Crear un control clínico (Necesita que se le de el id de la ficha del paciente al que se le asigna)
    @POST("controlClinico/{id}")
    suspend fun createControlClinico(@Path("id") id: String, @Body controlClinico: ControlClinico): Response<ControlClinico>
    @PATCH("controlClinico/{id}")
    suspend fun updateControlClinico(@Path("id") mongoId: String, @Body controlClinico: ControlClinico): Response<ControlClinico>
    @DELETE("controlClinico/{id}")
    suspend fun deleteControlClinico(@Path("id") mongoId: String): Response<Void>

    // ===== RECORD DE MEDIDAS ===== //
    // Obtener todos los registros de medidas
    @GET("recordMedidas")
    suspend fun getRecordMedidas(): Response<List<RecordMedidas>>
    // Obtener un registro de medidas por ID
    @GET("recordMedidas/{id}")
    suspend fun getRecordMedidasById(@Path("id") id: String): Response<List<RecordMedidas>>
    // Obtener un registro de medidas por fecha
    @GET("recordMedidas/date/{fecha}")
    suspend fun getRecordMedidasByDate(@Path("fecha") fecha: String): Response<List<RecordMedidas>>
    // Crear un registro de medidas (Necesita que se le de el id de la ficha del paciente al que se le asigna)
    @POST("recordMedidas/{id}")
    suspend fun createRecordMedidas(@Path("id") id: String, @Body recordMedidas: RecordMedidas): Response<RecordMedidas>
    // Actualizar un registro de medidas
    @PATCH("recordMedidas/{id}")
    suspend fun updateRecordMedidas(@Path("id") mongoId: String, @Body recordMedidas: RecordMedidas): Response<RecordMedidas>
    // Eliminar un registro de medidas
    @DELETE("recordMedidas/{id}")
    suspend fun deleteRecordMedidas(@Path("id") mongoId: String): Response<Void>

}
