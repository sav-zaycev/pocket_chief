package ru.zaycev.pocketchief.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.zaycev.pocketchief.R
import ru.zaycev.pocketchief.databinding.FragmentMainPageBinding
import ru.zaycev.pocketchief.viewmodel.MainViewModel

class MainPageFragment : Fragment(R.layout.fragment_main_page) {
    private lateinit var viewModel: MainViewModel

    private val binding by viewBinding(FragmentMainPageBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}