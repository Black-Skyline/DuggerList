package com.example.todolist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.R
import com.example.todolist.adapters.GateVpFragmentAdapter
import com.example.todolist.adapters.LazyCreateFragment
import com.example.todolist.databinding.ActivityGateBinding
import com.example.todolist.fragments.CalendarManageFragment
import com.example.todolist.fragments.CustomSettingFragment
import com.example.todolist.fragments.SimpleSearchFragment
import com.example.todolist.fragments.TaskDisplayFragment
import com.example.todolist.viewmodels.GateViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class GateActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityGateBinding.inflate(layoutInflater) }
    private val gateVP2: ViewPager2 get() = viewBinding.gateFragmentsVp
    private val naviView: BottomNavigationView get() = viewBinding.gateBottomNavi
    private val childFragmentList = ArrayList<LazyCreateFragment>()
    private val viewModel by lazy { ViewModelProvider(this)[GateViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initDatas()
        initView()
    }

    private fun initView() {
        gateVP2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        //  当切换底部导航选中项时，改变ViewPager的对应显示项
        naviView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_navi_task -> gateVP2.currentItem = 0
                R.id.bottom_navi_calendar -> gateVP2.currentItem = 1
                R.id.bottom_navi_search -> gateVP2.currentItem = 2
                R.id.bottom_navi_setting -> gateVP2.currentItem = 3
            }
            return@setOnItemSelectedListener true
        }
        gateVP2.adapter = GateVpFragmentAdapter(this, childFragmentList)
        gateVP2.isUserInputEnabled = false
    }

    private fun initDatas() {
        // init ChildFragmentList for GateActivity
        childFragmentList.add(noNameObject { TaskDisplayFragment() })
        childFragmentList.add(noNameObject { CalendarManageFragment() })
        childFragmentList.add(noNameObject { SimpleSearchFragment() })
        childFragmentList.add(noNameObject { CustomSettingFragment() })
    }

    private fun <T : Fragment> noNameObject(input_block: () -> T): LazyCreateFragment {
        return object : LazyCreateFragment {
            override fun backFragment(): Fragment {
                return input_block()
            }
        }
    }
}