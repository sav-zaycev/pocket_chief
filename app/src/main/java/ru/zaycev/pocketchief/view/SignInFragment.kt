package ru.zaycev.pocketchief.view

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import ru.zaycev.pocketchief.R
import ru.zaycev.pocketchief.databinding.FragmentSignInBinding
import ru.zaycev.pocketchief.viewmodel.AuthViewModel

class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private lateinit var viewModel: AuthViewModel

    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInEditEmail.addTextChangedListener {
            viewModel.inputEmail(binding.signInEditEmail.text.toString())
        }

        binding.signInEditPassword.addTextChangedListener {
            viewModel.inputPassword(binding.signInEditPassword.text.toString())
        }

        binding.signInButton.setOnClickListener {
            viewModel.signInUser()
        }

        viewModel.iconEmailValidation.observe(viewLifecycleOwner) {
            binding.signInLayoutEmail.endIconDrawable = it
        }

        viewModel.errorText.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.authSuccess.observe(viewLifecycleOwner) {
            if (it) findNavController().navigate(R.id.action_authenticationFragment_to_mainPageFragment)
        }

    }
}

