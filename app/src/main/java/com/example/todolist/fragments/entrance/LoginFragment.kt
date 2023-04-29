package com.example.todolist.fragments.entrance

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.databinding.FragmentEntranceLoginBinding
import com.example.todolist.fragments.BaseViewBindingFG
import com.example.todolist.viewmodels.AccountDataViewModel

class LoginFragment :
    BaseViewBindingFG<FragmentEntranceLoginBinding>(FragmentEntranceLoginBinding::inflate) {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SavedStateViewModelFactory(requireActivity().application, this)
        )[AccountDataViewModel::class.java]
    }

}