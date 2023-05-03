package ru.zaycev.pocketchief.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.zaycev.pocketchief.R
import ru.zaycev.pocketchief.view.adapter.PasswordRequirementsAdapter
import ru.zaycev.pocketchief.view.data.PasswordRequirement
import ru.zaycev.pocketchief.view.decorator.MarginItemDecorator

class RegFragment : Fragment() {
    private var password: String = ""
    private var email: String = ""
    private var passwordIsTrue: Boolean = false
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailEdit: TextInputEditText
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var buttonReg: Button
    private val recyclerView: RecyclerView by lazy { requireView().findViewById(R.id.regPasswordRequirements) }

    private val requirements: ArrayList<PasswordRequirement> by lazy {
        arrayListOf(
            PasswordRequirement(requireActivity().resources.getString(R.string.require_one)),
            PasswordRequirement(requireActivity().resources.getString(R.string.require_two)),
            PasswordRequirement(requireActivity().resources.getString(R.string.require_three)),
            PasswordRequirement(requireActivity().resources.getString(R.string.require_four))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reg, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = PasswordRequirementsAdapter(requirements)

            addItemDecoration(
                MarginItemDecorator(marginTop = 16, marginBottom = 16)
            )
        }

        emailInputLayout = requireView().findViewById(R.id.regLayoutEmail)
        emailEdit = requireView().findViewById(R.id.regEditEmail)
        emailEdit.addTextChangedListener {
            email = emailEdit.text.toString()

            emailInputLayout.endIconDrawable =
                if (email.isEmpty()) {
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_transparent, null)
                } else if (isEmailValid(email)) {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_check, null)
                } else {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_warning, null)
                }
        }

        passwordEdit = requireView().findViewById(R.id.regEditPassword)
        passwordEdit.addTextChangedListener {
            password = passwordEdit.text.toString()

            requirements[0].isRight = password.length > 7
            requirements[1].isRight = Regex("[0-9][^А-ЯЁа-яё]|[^А-ЯЁа-яё][0-9]").containsMatchIn(password)
            requirements[2].isRight = Regex("[A-Z]").containsMatchIn(password)
            requirements[3].isRight = Regex("[!$&amp.-/*]").containsMatchIn(password)

            passwordIsTrue = requirements[0].isRight && requirements[1].isRight && requirements[2].isRight && requirements[3].isRight

            recyclerView.adapter!!.notifyDataSetChanged()
        }

        buttonReg = requireView().findViewById(R.id.regButton)
        buttonReg.setOnClickListener {
            if (passwordIsTrue && isEmailValid(email)) {
                Log.d("Registration", "Регистрация прошла успешно")
            }
        }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}