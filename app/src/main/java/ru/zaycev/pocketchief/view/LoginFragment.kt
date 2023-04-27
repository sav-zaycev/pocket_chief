package ru.zaycev.pocketchief.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.zaycev.pocketchief.R

class LoginFragment : Fragment() {
    private lateinit var loginEdit: TextInputEditText
    private lateinit var loginInputLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginEdit = requireView().findViewById(R.id.loginEditEmail)
        loginInputLayout = requireView().findViewById(R.id.loginLayoutEmail)

        loginEdit.addTextChangedListener {
            val login: String = loginEdit.text.toString()

            loginInputLayout.endIconDrawable =
                if (login.isEmpty()) {
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_transparent, null)
                } else if (isEmailValid(login)) {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_check, null)
                } else {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_warning, null)
                }
        }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

