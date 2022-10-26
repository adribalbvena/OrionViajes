package ar.edu.ort.orionviajes

import okhttp3.ResponseBody


sealed class Resource<out T>() {
     class Success<out T>(value: Resource<T>) : Resource<T>()
     class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>()
}
