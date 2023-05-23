package ru.zaycev.pocketchief.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.zaycev.pocketchief.R
import ru.zaycev.pocketchief.databinding.FragmentRegBinding
import ru.zaycev.pocketchief.view.adapter.VerificationRequirementsAdapter
import ru.zaycev.pocketchief.view.decorator.MarginItemDecorator
import ru.zaycev.pocketchief.viewmodel.AuthViewModel

class RegFragment : Fragment(R.layout.fragment_reg) {
    private lateinit var viewModel: AuthViewModel

    private val binding by viewBinding(FragmentRegBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customAdapter = VerificationRequirementsAdapter()

        binding.regEditEmail.addTextChangedListener {
            viewModel.inputEmail(binding.regEditEmail.text.toString())
        }

        binding.regEditPassword.addTextChangedListener {
            viewModel.inputPassword(binding.regEditPassword.text.toString())
        }

        binding.regPasswordRequirements.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = customAdapter

            addItemDecoration(
                MarginItemDecorator(marginTop = 16, marginBottom = 16)
            )
        }

        binding.regButton.setOnClickListener {
            viewModel.registrationUser()
        }

        viewModel.requirements.observe(viewLifecycleOwner) {
            customAdapter.setData(it)
        }

        viewModel.iconEmailValidation.observe(viewLifecycleOwner) {
            binding.regLayoutEmail.endIconDrawable = it
        }

        viewModel.authSuccess.observe(viewLifecycleOwner) {
            if (it) findNavController().navigate(R.id.action_authenticationFragment_to_mainPageFragment)
        }

        viewModel.errorText.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}