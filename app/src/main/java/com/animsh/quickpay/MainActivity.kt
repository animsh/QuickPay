package com.animsh.quickpay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.animsh.quickpay.databinding.ActivityMainBinding
import com.animsh.quickpay.ui.main.bag.BagFragment
import com.animsh.quickpay.ui.main.home.HomeFragment
import com.animsh.quickpay.ui.main.profile.ProfileFragment
import com.animsh.quickpay.ui.main.scanner.ScannerFragment
import com.animsh.quickpay.view.TabFlashyAnimator
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mFragmentList: MutableList<Fragment> = ArrayList()
    private lateinit var tabFlashyAnimator: TabFlashyAnimator
    private val titles = arrayOf("Home", "Scanner", "Bag", "Profile")
    private var _backstack: Stack<Int> = Stack()
    private var saveToHistory by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.apply {
            mFragmentList.add(HomeFragment())
            mFragmentList.add(ScannerFragment())
            mFragmentList.add(BagFragment())
            mFragmentList.add(ProfileFragment())
            val adapter: FragmentStatePagerAdapter = object : FragmentStatePagerAdapter(
                supportFragmentManager
            ) {
                override fun getItem(position: Int): Fragment {
                    return mFragmentList[position]
                }

                override fun getCount(): Int {
                    return mFragmentList.size
                }
            }

            _backstack.push(0)
            viewpager.setOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageSelected(arg0: Int) {
                    if (saveToHistory) {
                        if (_backstack.empty())
                            _backstack.push(0)
                        _backstack.push(Integer.valueOf(arg0))
                    }
                }

                override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
                override fun onPageScrollStateChanged(arg0: Int) {}
            })

            saveToHistory = true
            viewpager.adapter = adapter
            tabLayout.setupWithViewPager(viewpager)

            tabFlashyAnimator = TabFlashyAnimator(tabLayout)
            tabFlashyAnimator.addTabItem(titles[0], R.drawable.ic_home, R.color.colorPrimaryDark)
            tabFlashyAnimator.addTabItem(titles[1], R.drawable.ic_scanner, R.color.colorPrimaryDark)
            tabFlashyAnimator.addTabItem(titles[2], R.drawable.ic_bag, R.color.colorPrimaryDark)
            tabFlashyAnimator.addTabItem(titles[3], R.drawable.ic_profile, R.color.colorPrimaryDark)
            tabFlashyAnimator.highLightTab(0)
            viewpager.addOnPageChangeListener(tabFlashyAnimator)

        }
    }

    override fun onStart() {
        super.onStart()
        tabFlashyAnimator.onStart(binding.tabLayout)
    }

    override fun onStop() {
        super.onStop()
        tabFlashyAnimator.onStop()
    }

    override fun onBackPressed() {
        if (_backstack.empty())
            super.onBackPressed()
        else {
            saveToHistory = false
            _backstack.pop()
            if (!_backstack.empty())
                binding.viewpager.currentItem = _backstack.lastElement().toInt()
            else
                Toast.makeText(
                    applicationContext,
                    "Press back one more time to exit!!",
                    Toast.LENGTH_SHORT
                ).show()
            saveToHistory = true
        }
    }
}