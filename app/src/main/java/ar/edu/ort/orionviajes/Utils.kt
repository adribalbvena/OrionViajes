package ar.edu.ort.orionviajes
//
//import android.view.View
//import androidx.fragment.app.Fragment
//import ar.edu.ort.orionviajes.fragments.LoginFragment
//import com.google.android.material.snackbar.Snackbar
//
//fun View.snackbar(message: String, action: (() -> Unit)? = null) {
//    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
//    action?.let {
//        snackbar.setAction("Reintentar"){
//            it()
//        }
//    }
//    snackbar.show()
//}
//
//fun Fragment.handleApiError(
//    failure: Resource.Failure,
//    retry: (() -> Unit)? = null
//){
//    when{
//        failure.isNetworkError -> requireView().snackbar("Por favor revisa tu conexión a internet", retry)
//        failure.errorCode == 401 -> {
//            if(this is LoginFragment){
//                requireView().snackbar("Credenciales invalidas")
//            } else {
//                //@todo poner el logout aca
//            }
//        }
//        else -> {
//            val error = failure.errorBody?.string().toString()
//            requireView().snackbar("Algo salió mal")
//        }
//
//    }
//
//}