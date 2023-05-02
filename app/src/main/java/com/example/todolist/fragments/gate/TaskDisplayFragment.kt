package com.example.todolist.fragments.gate

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.adapters.FoldListAdapter
import com.example.todolist.databinding.FragmentTaskDisplayBinding
import com.example.todolist.fragments.BaseViewBindingFG
import com.example.todolist.viewmodels.GateViewModel


class TaskDisplayFragment :
    BaseViewBindingFG<FragmentTaskDisplayBinding>(FragmentTaskDisplayBinding::inflate) {
    private val viewModel by lazy { ViewModelProvider(this)[GateViewModel::class.java] }

    private val foldData = ArrayList<FoldListAdapter.FoldData>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatas()
        binding.taskDisplayList.adapter = FoldListAdapter(foldData)
    }

    private fun initDatas() {
        for (i in 0..5) {
            val parentBean =
                FoldListAdapter.FoldData.ParentBean(title = "$i -> parent", isExpand = (i == 0))
            val children = ArrayList<FoldListAdapter.FoldData.ChildBean>()
            for (j in 0..10) {
                val childBean = FoldListAdapter.FoldData.ChildBean(title = "$j -> child")
                children.add(childBean)
            }
            parentBean.children = children
            foldData.add(parentBean)
        }
    }
}