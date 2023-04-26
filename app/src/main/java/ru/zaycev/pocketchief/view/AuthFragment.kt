package ru.zaycev.pocketchief.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import ru.zaycev.pocketchief.R
import ru.zaycev.pocketchief.view.adapter.FragmentPageAdapter

class AuthFragment : Fragment() {
    private val tabLayout: TabLayout by lazy { requireView().findViewById(R.id.authTabLayout) }
    private val viewPager: ViewPager2 by lazy { requireView().findViewById(R.id.authViewPager) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout.setTabs(arrayOf(
            "Вход",
            "Регистрация"
        ))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {  }

            override fun onTabReselected(tab: TabLayout.Tab?) {  }
        })

        viewPager.adapter = FragmentPageAdapter(childFragmentManager, lifecycle)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    private fun TabLayout.setTabs(tabsName: Array<String>) {
        for (index in tabsName) {
            this.addTab(this.newTab().setText(index))
        }
    }
}