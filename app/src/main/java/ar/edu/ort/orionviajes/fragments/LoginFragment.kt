package ar.edu.ort.orionviajes.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.SessionManager
import ar.edu.ort.orionviajes.activities.MainActivity
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.UserDto
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    private lateinit var emailInput: EditText
    private lateinit var emailContainter: TextInputLayout
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var buttonRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_login, container, false);

        emailInput = view.findViewById(R.id.editText_email_login)
        emailContainter = view.findViewById(R.id.loginEmail_til)
        passwordInput = view.findViewById(R.id.editText_password_login)
        loginButton = view.findViewById(R.id.button_login)
        buttonRegister = view.findViewById(R.id.buttonRegister)

        emailFocusListener()

        return view;
    }

    override fun onStart() {
        super.onStart()

        loginButton.setOnClickListener {
            onLoginPressed()
        }

        buttonRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment2()
            findNavController().navigate(action)
        }
    }

    private fun emailFocusListener(){
        emailInput.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                emailContainter.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = emailInput.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return R.string.invalidEmail.toString()
        }
        return null
    }

    private fun onLoginPressed() {
        if (!validateInputs()) {
            return
        }

        resolveLogin()
    }

    private fun resolveLogin() {
        var user = UserDto(emailInput.text.toString(), passwordInput.text.toString())
        val call = ApiClient.getUsersApi().login(user)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                onLoginResponse(response)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                onLoginFailure(t)
            }
        })
    }

    private fun validateInputs(): Boolean {
        if (TextUtils.isEmpty(emailInput.text.toString()) || TextUtils.isEmpty(passwordInput.text.toString())) {
            Snackbar.make(requireView(), R.string.emptyEmailandPass, Snackbar.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun onLoginResponse(response: Response<String>) {
        if (response.isSuccessful) {
            storeToken(response.body().toString())
            this.activity?.startActivity(Intent(this.activity, MainActivity::class.java))
            this.activity?.finish()
            //setFragmentResult("login", bundleOf("bundleKey" to "asd"))
        } else {
            if (response.code() == 401) {
                Snackbar.make(requireView(), R.string.invalidCredentials, Snackbar.LENGTH_LONG).show()
            }
            Log.e(TAG, response.body().toString())
        }
    }

    private fun onLoginFailure(t: Throwable) {
        Snackbar.make(requireView(), R.string.somethingWentWrong, Snackbar.LENGTH_LONG).show()
        Log.e(TAG, t.toString())
    }

    private fun storeToken(token: String) {
        activity?.let { SessionManager(it).saveAuthToken(token) }
    }

    companion object {
        private const val TAG = "Login Fragment"
    }
}