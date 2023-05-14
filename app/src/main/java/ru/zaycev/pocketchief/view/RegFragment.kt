package ru.zaycev.pocketchief.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.zaycev.pocketchief.R
import ru.zaycev.pocketchief.view.adapter.VerificationRequirementsAdapter
import ru.zaycev.pocketchief.view.decorator.MarginItemDecorator
import ru.zaycev.pocketchief.viewmodel.AuthViewModel

class RegFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel

    private var password: String = ""
    private var email: String = ""

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailEdit: TextInputEditText

    private lateinit var passwordEdit: TextInputEditText
    private lateinit var buttonReg: Button

    private val recyclerView: RecyclerView by lazy { requireView().findViewById(R.id.regPasswordRequirements) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reg, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customAdapter = VerificationRequirementsAdapter()
        emailInputLayout = requireView().findViewById(R.id.regLayoutEmail)
        emailEdit = requireView().findViewById(R.id.regEditEmail)
        passwordEdit = requireView().findViewById(R.id.regEditPassword)
        buttonReg = requireView().findViewById(R.id.regButton)

        recyclerView.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = customAdapter

            addItemDecoration(
                MarginItemDecorator(marginTop = 16, marginBottom = 16)
            )
        }

        emailEdit.addTextChangedListener {
            email = emailEdit.text.toString().trim()
            emailInputLayout.endIconDrawable =
                if (email.isEmpty()) {
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_transparent, null)
                } else if (viewModel.isEmailValid(email)) {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_check, null)
                } else {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_warning, null)
                }
        }

        passwordEdit.addTextChangedListener {
            password = passwordEdit.text.toString().trim()

            viewModel.updateStateRequirements(password)
        }

        viewModel.requirements.observe(requireActivity()) {
            customAdapter.setData(it)
        }

        buttonReg.setOnClickListener {
            viewModel.newRegistrationUser(email, password)
        }
    }



//    private fun registrationUser(email: String, password: String) {
//        // Проверяю соединение с интернетом, в случае неудачи показываю предупреждение
//        if (viewModel.inNetworkAvailable()) {
//            // Проверяю соответствие логина и пароля, в случае неудачи показываю предупреждение
//            if (viewModel.isEmailValid(email) && viewModel.isPasswordValid()) {
//                // Произвожу регистрацию, в случае возврата перехожу на главную страницу, иначе
//                // показываю соответствующее предупреждение
//                viewModel.registrationUser(email, password) { authResult: Boolean ->
//                    // Использую обратный вызов для получения Task (результат регистрации),
//                    // поскольку регистрации производит асинхронно, путем обратного вызова
//                    if (authResult) {
//                        findNavController().navigate(R.id.action_authenticationFragment_to_mainPageFragment)
//                    } else {
//                        Toast.makeText(context,"Пользователь с указанным адресом электронной почты уже зарегистрирован!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } else {
//                Toast.makeText(context,"Пожалуйста, проверьте правильность введенных данных.", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(context, "Пожалуйста, проверьте подключение к интернету и повторите попытку.", Toast.LENGTH_SHORT).show()
//        }
//    }
}