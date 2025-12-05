package vrz.JoacoVrz.sabercomer_androidapp.FrontEnd.utils

// Clase sellada para manejar los estados de las peticiones a la API
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}