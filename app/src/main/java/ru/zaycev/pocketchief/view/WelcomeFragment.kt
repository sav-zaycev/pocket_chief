package ru.zaycev.pocketchief.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import ru.zaycev.pocketchief.R

class WelcomeFragment : Fragment() {
    private lateinit var continueButton: Button

    private val shared: SharedPreferences by lazy {
        requireContext().getSharedPreferences("FirstOpenCheck", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        continueButton = requireView().findViewById(R.id.welcomeButton)

        continueButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_authenticationFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        if (shared.getBoolean("isFirstOpen", false)) {
            findNavController().navigate(R.id.action_welcomeFragment_to_authenticationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shared.edit().putBoolean("isFirstOpen", true).apply()
    }
}
