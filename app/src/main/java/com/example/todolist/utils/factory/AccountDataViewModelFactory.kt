package com.example.todolist.utils.factory

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.viewmodels.AccountDataViewModel


class AccountDataViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountDataViewModel::class.java)) {
            return AccountDataViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




