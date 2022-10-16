package ar.edu.ort.orionviajes.interceptors

import android.content.Context
import ar.edu.ort.orionviajes.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    private companion object {
        const val TOKEN_HEADER_KEY = "x-auth-token"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader(TOKEN_HEADER_KEY, "$it")
        }

        return chain.proceed(requestBuilder.build())
    }
}