package com.example.todolist.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.todolist.R

class AccountDataViewModel(application: Application, private val handle: SavedStateHandle) :
    AndroidViewModel(application) {

    private val mApplication get() = getApplication<Application>()

    private val _loginAccount = MutableLiveData<String>()
    val loginAccount: LiveData<String> get() = _loginAccount
    private val _loginPassword = MutableLiveData<String>()
    val loginPassword: LiveData<String> get() = _loginPassword
    private val _registerAccount = MutableLiveData<String>()
    val registerAccount: LiveData<String> get() = _registerAccount
    private val _registerPassword = MutableLiveData<String>()
    val registerPassword: LiveData<String> get() = _registerPassword
    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> get() = _confirmPassword

    fun setLoginAccount(value: String) {
        _loginAccount.value = value
    }

    fun setLoginPassword(value: String) {
        _loginPassword.value = value
    }

    fun setRegisterAccount(value: String) {
        _registerAccount.value = value
    }

    fun setRegisterPassword(value: String) {
        _registerPassword.value = value
    }

    fun setConfirmPassword(value: String) {
        _confirmPassword.value = value
    }

    companion object {
        private val isRememberPasswdKey =
            instance.mApplication.resources.getString(R.string.remember_passwd_key)
        private val accountKey =
            instance.mApplication.resources.getString(R.string.account_key)
        private val passwordKey =
            instance.mApplication.resources.getString(R.string.password_data_key)
        private val shpName =
            instance.mApplication.resources.getString(R.string.account_sharePref_name)

        private lateinit var instance: AccountDataViewModel

        fun initInstance(externalInstance: AccountDataViewModel) {
            instance = externalInstance
        }

        fun getIsRememberPasswdKey() = isRememberPasswdKey
    }

    init {
        if (!handle.contains(accountKey)) readLoad(accountKey)
        if (!handle.contains(passwordKey)) readLoad(passwordKey)
        if (!handle.contains(isRememberPasswdKey)) readLoad(isRememberPasswdKey)
    }

    // 从本地存储中读取数据，放入handle中
    private fun readLoad(key: String) {
        val shp = mApplication.getSharedPreferences(shpName, Context.MODE_PRIVATE)
        when (key) {
            isRememberPasswdKey -> handle[key] = shp.getBoolean(key, false)
            accountKey -> handle[key] = shp.getString(key, "账号不存在")
            passwordKey -> handle[key] = shp.getString(key, "密码不存在")
            else -> Log.e(Log.ERROR.toString(), "error in read datas from Disk")
        }
    }

    // 将现有数据存入本地存储
    private fun saveSent(key: String, value: String = "", switch: Boolean = false): Boolean {
        if (key != isRememberPasswdKey) {
            if (TextUtils.isEmpty(value.trim { it <= ' ' })) {
                return false
            }
            mApplication.getSharedPreferences(shpName, Context.MODE_PRIVATE).edit {
                putString(key, value)
            }
        } else {
            mApplication.getSharedPreferences(shpName, Context.MODE_PRIVATE).edit {
                putBoolean(key, switch)
            }
        }
        return true
    }

    fun updateShp(key: String): Boolean = when (key) {
        isRememberPasswdKey -> saveSent(isRememberPasswdKey, switch = getIsRememberPasswdValue())
        accountKey -> saveSent(accountKey, value = getAccountValue())
        passwordKey -> saveSent(passwordKey, value = getPasswordValue())
        else -> false
    }


    fun checkIsRememberPasswd() {
        if (getIsRememberPasswd().value!!) {
            getAccount().value?.let { setLoginAccount(it) }
            getPassword().value?.let { setLoginPassword(it) }
        }
    }

    private fun updateHandlerAllData() {
        readLoad(accountKey)
        readLoad(passwordKey)
        readLoad(isRememberPasswdKey)
    }

    private fun saveRegisterData(): Boolean {
        return registerAccount.value!!.run {
            registerPassword.value!!.apply {
                handle[accountKey] = this@run
                handle[passwordKey] = this
            }
            updateShp(accountKey) && updateShp(passwordKey)
        }
    }


    fun loginCheck(): Boolean {
        if (getAccountValue() == _loginAccount.value) {
            if (getPasswordValue() == _loginPassword.value)
                return true
            else Toast.makeText(
                mApplication,
                "e u-o-u, 密码好像输错了呢~",
                Toast.LENGTH_SHORT
            ).show()
        } else Toast.makeText(mApplication, "这个账号达咩哒~~~", Toast.LENGTH_SHORT).show()
        return false
    }

    fun userClicked(v: View) {
        when (v.id) {
            R.id.confirm_register -> {
                if (!(registerAccount.value.isNullOrBlank())) {
                    if (!(registerPassword.value.isNullOrBlank())) {
                        if (!(confirmPassword.value.isNullOrBlank())) {
                            if (saveRegisterData())
                                Toast.makeText(mApplication, "注册成功", Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(
                                    mApplication, "注册失败!请重新注册……", Toast.LENGTH_SHORT
                                ).show()
                        } else Toast.makeText(getApplication(), "密码不一致", Toast.LENGTH_SHORT).show()
                    } else Toast.makeText(getApplication(), "请设置密码！", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(getApplication(), "账号不能为空！", Toast.LENGTH_SHORT).show()
            }
            R.id.back_login_page -> {

            }
            else -> {

            }
        }
    }

    fun clearLiveData() {
        setRegisterAccount("")
        setRegisterPassword("")
        setConfirmPassword("")
    }


    fun changeIsRememberPasswd(): Boolean {
        handle[isRememberPasswdKey] = !(getIsRememberPasswd().value!!)
        return getIsRememberPasswd().value!!
    }

    fun getAccount(): LiveData<String> {
        return handle.getLiveData(accountKey)
    }

    fun getAccountValue(): String {
        return handle.get<String>(accountKey).toString()
    }

    fun getPassword(): LiveData<String> {
        return handle.getLiveData(passwordKey)
    }

    fun getPasswordValue(): String {
        return handle.get<String>(passwordKey).toString()
    }

    fun getIsRememberPasswd(): LiveData<Boolean> {
        return handle.getLiveData(isRememberPasswdKey)
    }

    fun getIsRememberPasswdValue(): Boolean {
        return handle.get<Boolean>(isRememberPasswdKey)!!
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    fun setDrawableLeft(v: EditText, color_id: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            v.compoundDrawableTintList = getApplication<Application>().getColorStateList(color_id)
            // 如果需要设置 mode
            // Java中
            // textView.setCompoundDrawableTintMode(mode);
        } else {
            val drawable = v.compoundDrawables[0]
            val left = tintListDrawable(
                drawable,
                ContextCompat.getColorStateList(getApplication(), color_id)
            )
            v.setCompoundDrawables(left, null, null, null)
        }
    }

    private fun getCanTintDrawable(drawable: Drawable): Drawable {
        // 获取此drawable的共享状态实例
        val state = drawable.constantState
        // 对drawable 进行重新实例化、包装、可变操作
        return DrawableCompat.wrap(state?.newDrawable() ?: drawable).mutate()
    }

    private fun tintListDrawable(drawable: Drawable, colors: ColorStateList?): Drawable {
        val wrappedDrawable = getCanTintDrawable(drawable)
        // 进行着色
        DrawableCompat.setTintList(wrappedDrawable, colors)
        // 如果需要设置 mode
        //DrawableCompat.setTintMode(wrappedDrawable, mode);
        return wrappedDrawable
    }
}