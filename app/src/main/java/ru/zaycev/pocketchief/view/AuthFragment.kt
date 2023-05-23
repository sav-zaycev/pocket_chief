package ru.zaycev.pocketchief.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import ru.zaycev.pocketchief.R
import ru.zaycev.pocketchief.databinding.FragmentAuthBinding
import ru.zaycev.pocketchief.view.adapter.FragmentPageAdapter
import ru.zaycev.pocketchief.viewmodel.AuthViewModel


class AuthFragment : Fragment(R.layout.fragment_auth) {
    private lateinit var viewModel: AuthViewModel

    private val binding by viewBinding(FragmentAuthBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkUserSignIn()

        viewModel.authSuccess.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_authenticationFragment_to_mainPageFragment)
        }

        binding.authTabLayout.run {
            setTabs(arrayOf("Вход", "Регистрация"))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) binding.authViewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {  }
                override fun onTabReselected(tab: TabLayout.Tab?) {  }
            })
        }

        binding.authViewPager.run {
            adapter = FragmentPageAdapter(childFragmentManager, lifecycle)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.authTabLayout.selectTab(binding.authTabLayout.getTabAt(position))
                }
            })
        }
    }

    private fun TabLayout.setTabs(tabsName: Array<String>) {
        for (index in tabsName) {
            this.addTab(this.newTab().setText(index))
        }
    }
}