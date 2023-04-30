package com.example.todolist.utils

import androidx.databinding.ViewDataBinding

interface BaseBinding<VB : ViewDataBinding> {
    fun VB.initBinding()
}