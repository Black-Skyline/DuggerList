package com.example.todolist.databeans.user

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class AccountData {
    val loginAccount = ObservableField<String>()
    val loginPasswd = ObservableField<String>()
    val registerAccount = ObservableField<String>()
    val registerPasswd = ObservableField<String>()
    val confirmPasswd = ObservableField<String>()
    val isRememberPasswd = ObservableBoolean()
}