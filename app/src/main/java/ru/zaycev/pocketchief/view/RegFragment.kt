package ru.zaycev.pocketchief.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.zaycev.pocketchief.R
import ru.zaycev.pocketchief.view.adapter.PasswordRequirementsAdapter
import ru.zaycev.pocketchief.data.PasswordRequirement
import ru.zaycev.pocketchief.view.decorator.MarginItemDecorator

class RegFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth

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

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference
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
            requirements[1].isRight = Regex("[A-Za-z]").containsMatchIn(password) && Regex("[0-9]").containsMatchIn(password)
            requirements[2].isRight = Regex("[A-Z]").containsMatchIn(password)
            requirements[3].isRight = Regex("[$&.-/*]").containsMatchIn(password)

            passwordIsTrue = requirements[0].isRight && requirements[1].isRight && requirements[2].isRight && requirements[3].isRight

            recyclerView.adapter!!.notifyDataSetChanged()
        }

        buttonReg = requireView().findViewById(R.id.regButton)
        buttonReg.setOnClickListener {
            if (!inNetworkAvailable(requireContext())) {
                Toast.makeText(requireContext(), "Отсутствует подключение к сети!", Toast.LENGTH_LONG).show()
            } else if (passwordIsTrue && isEmailValid(email)) {
                registrationUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, провереться правильность введенных данных.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun addUserToDataBase(email: String) {
        database.child("users").child("user").child("email").setValue(email)
    }

    private fun registrationUser(email: String, password: String) {
        mAuth = FirebaseAuth.getInstance()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    addUserToDataBase(email)
                    findNavController().navigate(R.id.action_authenticationFragment_to_mainPageFragment)
                } else {
                    Toast.makeText(context, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun inNetworkAvailable(aContext: Context): Boolean {
        val connectivityManager: ConnectivityManager = aContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}