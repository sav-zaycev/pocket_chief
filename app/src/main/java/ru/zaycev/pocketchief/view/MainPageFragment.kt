package ru.zaycev.pocketchief.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ru.zaycev.pocketchief.R

class MainPageFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private var userID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        userID = mAuth.currentUser!!.uid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onResume() {
        super.onResume()

        Toast.makeText(requireContext(), userID, Toast.LENGTH_LONG).show()
    }
}