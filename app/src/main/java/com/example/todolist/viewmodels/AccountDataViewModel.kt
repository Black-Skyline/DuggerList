package com.example.todolist.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.todolist.R

class AccountDataViewModel(application: Application, private val handle: SavedStateHandle) :
    AndroidViewModel(application) {

    private val mApplication get() = getApplication<Application>()

    companion object {
        private val isRememberPasswdKey =
            instance.mApplication.resources.getString(R.string.remember_passwd_key)
        private val accountKey =
            instance.mApplication.resources.getString(R.string.account_sharePref_name)
        private val userNameKey =
            instance.mApplication.resources.getString(R.string.user_name_data_key)
        private val passwordKey =
            instance.mApplication.resources.getString(R.string.password_data_key)
        private val shpName =
            instance.mApplication.resources.getString(R.string.account_sharePref_name)

        private lateinit var instance: AccountDataViewModel

        fun initInstance(externalInstance: AccountDataViewModel) {
            instance = externalInstance
        }
    }

    init {
        if (!handle.contains(userNameKey)) readLoad(userNameKey)
        if (!handle.contains(passwordKey)) readLoad(passwordKey)
        if (!handle.contains(isRememberPasswdKey)) readLoad(isRememberPasswdKey)
    }

    // 从本地存储中读取数据，放入handle中
    private fun readLoad(key: String) {
        val shp = mApplication.getSharedPreferences(shpName, Context.MODE_PRIVATE)
        when (key) {
            isRememberPasswdKey -> handle[key] = shp.getBoolean(key, false)
            userNameKey -> handle[key] = shp.getString(key, "用户名不存在")
            accountKey -> handle[key] = shp.getString(key, "账号不存在")
            passwordKey -> handle[key] = shp.getString(key, "密码不存在")
            else -> Log.e(Log.ERROR.toString(), "error in read datas from Disk")
        }
    }

    // 将现有数据存入本地存储
    private fun saveSent(key: String, value: String): Boolean {
        if (TextUtils.isEmpty(value.trim { it <= ' ' })) {
            return false
        }

        mApplication.getSharedPreferences(shpName, Context.MODE_PRIVATE).edit {
            putString(key, value)
        }
        return true
    }
}