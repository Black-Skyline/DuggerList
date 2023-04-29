package com.example.todolist.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

interface LazyCreateFragment {
    fun backFragment(): Fragment
}

class GateVpFragmentAdapter(
    private val container: FragmentActivity,
    private val fragmentList: ArrayList<LazyCreateFragment>
) : FragmentStateAdapter(container) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position].backFragment()
}