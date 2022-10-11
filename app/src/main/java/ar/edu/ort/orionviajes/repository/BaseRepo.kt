package ar.edu.ort.orionviajes.repository

import ar.edu.ort.orionviajes.Resource
import ar.edu.ort.orionviajes.data.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepo {

    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>) : Resource<T> {
        return withContext(Dispatchers.IO) {
            try{
                val response: Response<T> = apiToBeCalled()
                if (response.isSuccessful) {
                    Resource.Success(data = response.body()!!)
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    Resource.Error(
                        errorMessage = errorResponse?.statusMessage ?: "Algo salió mal"
                    )
                }
            } catch (e: HttpException) {
                Resource.Error(errorMessage = e.message ?: "Algo salió mal")
            } catch (e: IOException) {
                Resource.Error("Por favor revisa tu conexión a internet")
            } catch (e: Exception) {
                Resource.Error(errorMessage = "Algo salió mal")
            }
        }
    }
}