package com.example.todolist.utils

import android.text.Editable
import android.text.TextWatcher
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

abstract class BaseTextChangedWatcher : TextWatcher {
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {}
}

class CurrentDate : Date() {
    private val currentDate = Date()
    private val formAll = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val formMD = SimpleDateFormat("MM-dd", Locale.getDefault())
    private val formYMD = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    fun allForm(): String = formAll.format(currentDate)
    fun mdForm(): String = formMD.format(currentDate)
    fun ymdForm(): String = formYMD.format(currentDate)

}