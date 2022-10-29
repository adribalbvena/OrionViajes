package ar.edu.ort.orionviajes.fragments

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

class RegisterFragment : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var emailContainter: TextInputLayout
    private lateinit var passwordInput: EditText
    private lateinit var passwordContainter: TextInputLayout
    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_register, container, false);

        emailInput = view.findViewById(R.id.editText_email_register)
        passwordInput = view.findViewById(R.id.editText_password_register)
        registerButton = view.findViewById(R.id.button_register)
        emailContainter = view.findViewById(R.id.registerEmail_til)
        passwordContainter = view.findViewById(R.id.registerPassword_til)

        emailFocusListener()
        passwordFocusListener()



        return view;
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
            return "Dirección de email inválida"
        }
        return null
    }

    private fun passwordFocusListener(){
        passwordInput.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                passwordContainter.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = passwordInput.text.toString()
        if(passwordText.length < 6) {
            return "Debe contener al menos 6 carácteres"
        }

        return null
    }

    override fun onStart() {
        super.onStart()

        registerButton.setOnClickListener {
            onRegisterPressed()
        }
    }

    private fun onRegisterPressed() {
        if (!validateInputs()) {
            return
        }

        resolveRegister()
    }

    private fun resolveRegister() {
        var user = UserDto(emailInput.text.toString(), passwordInput.text.toString())
        val call = ApiClient.getUsersApi().register(user)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                onRegisterResponse(response)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onRegisterFailure(t)
            }
        })
    }

    private fun validateInputs(): Boolean {
        if (TextUtils.isEmpty(emailInput.text.toString()) || TextUtils.isEmpty(passwordInput.text.toString())) {
            Snackbar.make(requireView(), R.string.emptyEmailandPass, Snackbar.LENGTH_LONG).show()
            return false
        } else if (validEmail() != null || validPassword() != null) {
            Snackbar.make(requireView(), R.string.invalidInputs, Snackbar.LENGTH_LONG).show()
            return false
        } else {
            return true
        }
    }

    private fun onRegisterResponse(response: Response<Unit>) {
        if (response.isSuccessful) {
            //storeToken(response.body().toString())
            Snackbar.make(requireView(), R.string.successRegister, Snackbar.LENGTH_LONG).show()
            findNavController().navigateUp()
            //setFragmentResult("login", bundleOf("bundleKey" to "asd"))
        } else {
            if (response.code() == 400) {
                Snackbar.make(requireView(), "Email en uso", Snackbar.LENGTH_LONG).show()
            }
            Log.e(TAG, response.body().toString())
        }
    }

    private fun onRegisterFailure(t: Throwable) {
        Snackbar.make(requireView(), R.string.somethingWentWrong, Snackbar.LENGTH_LONG).show()
        Log.e(TAG, t.toString())
    }

    private fun storeToken(token: String) {
        activity?.let { SessionManager(it).saveAuthToken(token) }
    }

    companion object {
        private val TAG = (RegisterFragment::class.java).name
    }

}