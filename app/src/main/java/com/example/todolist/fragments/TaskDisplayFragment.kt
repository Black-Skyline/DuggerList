package com.example.todolist.fragments

import androidx.lifecycle.ViewModelProvider
import com.example.todolist.databinding.FragmentTaskDisplayBinding
import com.example.todolist.viewmodels.GateViewModel

class TaskDisplayFragment :
    BaseViewBindingFG<FragmentTaskDisplayBinding>(FragmentTaskDisplayBinding::inflate) {
    private val viewModel by lazy { ViewModelProvider(this)[GateViewModel::class.java] }


}