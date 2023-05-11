package ru.zaycev.pocketchief.view

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import ru.zaycev.pocketchief.R

class SignInFragment : Fragment() {
    private lateinit var loginEdit: TextInputEditText
    private lateinit var loginInputLayout: TextInputLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var buttonSignIn: Button

    private lateinit var passwordEdit: TextInputEditText

    private var password: String = ""
    private var login: String = ""
    private var passwordIsTrue: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginEdit = requireView().findViewById(R.id.signInEditEmail)
        loginInputLayout = requireView().findViewById(R.id.signInLayoutEmail)

        loginEdit.addTextChangedListener {
            login = loginEdit.text.toString()

            loginInputLayout.endIconDrawable =
                if (login.isEmpty()) {
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_transparent, null)
                } else if (isEmailValid(login)) {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_check, null)
                } else {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_warning, null)
                }
        }

        passwordEdit = requireView().findViewById(R.id.signInEditPassword)
        passwordEdit.addTextChangedListener {
            password = passwordEdit.text.toString().trim()
        }

        buttonSignIn = requireView().findViewById(R.id.regButton)
        buttonSignIn.setOnClickListener {
            if (!(login.isEmpty() || password.isEmpty())) {
                signInUser(login, password)
            }
        }

    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun signInUser(email: String, password: String) {
        mAuth = FirebaseAuth.getInstance()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_authenticationFragment_to_mainPageFragment)
                } else {
                    Toast.makeText(context, "Ошибка входа", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

