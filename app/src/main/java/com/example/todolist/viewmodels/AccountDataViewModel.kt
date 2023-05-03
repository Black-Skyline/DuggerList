package com.example.todolist.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.todolist.R
import com.example.todolist.databeans.user.AccountData

/*
这个view model类的作用就是作为SharedPreferences和登录与注册界面的数据桥梁（用SavedStateHandle 类型的handle实现）
SharedPreferences最终存储的只有账号(String)、密码(String)、是否记住密码(Boolean)这三个值,
对应handle同样给出三个存储位置来中转就好了

这里要实现双向绑定,考虑到实际要获取每个输入框产生的数据这一需求，把每个框都设置双向绑定（共5个，由view的改变同步影响到数据）
这里需要注意：
单向绑定@{variable} 实际是调用了variable的getter方法，
相对应双向绑定@={variable}，当view的UI数据发生变更，会使用=和variable的setter方法把改变后的UI数据传入setter中，兼有单向绑定功能
 */
class AccountDataViewModel(application: Application, private val handle: SavedStateHandle) :
    AndroidViewModel(application) {

    private val mApplication get() = getApplication<Application>()
    private val isRememberPasswdKey = mApplication.resources.getString(R.string.remember_passwd_key)
    private val accountKey = mApplication.resources.getString(R.string.account_key)
    private val passwordKey = mApplication.resources.getString(R.string.password_data_key)
    private val shpName = mApplication.resources.getString(R.string.account_sharePref_name)

    /*
    * 这里我们需要实现从data -> view UI 的有: 单向@{}
    * 登录界面账号框
    * 登录界面密码框
    * 登录界面记住密码框
    *
    * 需要实现从UI data -> data 的有: 双向@={}
    * 注册界面账号框
    * 注册界面密码框
    * 注册界面确认密码框
    * 登录界面记住密码框
    *
    * */
    private val datas: AccountData = AccountData()
    fun getDatas(): AccountData = datas

    /*
    * 设置6个变量与对应的UI data通信, 通过get() 和 setValue()方法直接与UI data建立联系
    * */
    private val _loginAccount: String get() = datas.loginAccount.get().toString()
    private fun setLoginAccount(value: String) {
        datas.loginAccount.set(value)
    }

    private val _loginPassword: String get() = datas.loginPasswd.get().toString()
    private fun setLoginPassword(value: String) {
        datas.loginPasswd.set(value)
    }

    private val _isRememberPasswd: Boolean get() = datas.isRememberPasswd.get()
    private fun setIsRememberPasswd(value: Boolean) {
        datas.isRememberPasswd.set(value)
    }

    private val _registerAccount: String get() = datas.registerAccount.get().toString()
    private fun setRegisterAccount(value: String) {
        datas.registerAccount.set(value)
    }

    private val _registerPassword: String get() = datas.registerPasswd.get().toString()
    private fun setRegisterPassword(value: String) {
        datas.registerPasswd.set(value)
    }

    private val _confirmPassword get() = datas.confirmPasswd.get()
    private fun setConfirmPassword(value: String) {
        datas.confirmPasswd.set(value)
    }


    companion object {
        private lateinit var instance: AccountDataViewModel

        fun initInstance(externalInstance: AccountDataViewModel) {
            instance = externalInstance
        }

        fun getIsRememberPasswdKey() = instance.isRememberPasswdKey
    }

    // 这里对handle进行初始化，确定其初始值（令其每个数据位至少不为空）
    init {
        if (!handle.contains(accountKey)) readLoad(accountKey)
        if (!handle.contains(passwordKey)) readLoad(passwordKey)
        if (!handle.contains(isRememberPasswdKey)) readLoad(isRememberPasswdKey)
    }

    // 从本地存储中读取数据，放入handle中
    private fun readLoad(key: String) {
        val shp = mApplication.getSharedPreferences(shpName, Context.MODE_PRIVATE)
        val blankStr = ""
        when (key) {
            isRememberPasswdKey -> handle[key] = shp.getBoolean(key, false)
            accountKey -> handle[key] = shp.getString(key, blankStr)
            passwordKey -> handle[key] = shp.getString(key, blankStr)
            else -> Log.e(Log.ERROR.toString(), "error in read datas from Disk")
        }
    }

    // 将现有数据存入本地存储
    private fun saveSent(key: String, value: String = "", switch: Boolean = false): Boolean {
        if (key != isRememberPasswdKey) {
            if (value.isBlank()) {
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

    // 把三种重要的数据从 当前 handler 中写入 SharedPreferences里，返回Boolean确认是否写入成功
    fun updateShp(key: String): Boolean = when (key) {
        isRememberPasswdKey -> {
            handle[isRememberPasswdKey] = _isRememberPasswd
            saveSent(isRememberPasswdKey, switch = getIsRememberPasswdValue())
        }

        accountKey -> saveSent(accountKey, value = getAccountValue())
        passwordKey -> saveSent(passwordKey, value = getPasswordValue())
        else -> false
    }

    // 对当前 handler里的 isRememberPasswd变量进行检查，检查通过就把当前handler里的account和password数据放到 UI上
    // 在登录界面被创建是调用,给UI data初始值
    fun checkIsRememberPasswd() {
        if (getIsRememberPasswdValue()) {
            setLoginAccount(getAccountValue())
            setLoginPassword(getPasswordValue())
            setIsRememberPasswd(true)
        } else {
            setLoginAccount("")
            setLoginPassword("")
            setIsRememberPasswd(false)
        }
    }

    // 从SharedPreferences里 把当前的所有handler（3个）数据更新一遍
    private fun updateHandlerAllData() {
        readLoad(accountKey)
        readLoad(passwordKey)
        readLoad(isRememberPasswdKey)
    }

    // 保存注册数据的操作，确保注册界面数据符合要求后再调用,
    // 顺便把注册数据更新到 handler 里,把 handler里的数据更新到 SharedPreferences里
    private fun saveRegisterData(): Boolean {
        return _registerAccount.run {
            _registerPassword.apply {
                handle[accountKey] = this@run
                handle[passwordKey] = this@apply
            }
            updateShp(accountKey) && updateShp(passwordKey)
        }
    }


    // 用当前handler里的数据 对登陆界面输入的登录数据(UI data)检查
    // 返回检查是否通过的结果
    fun loginCheck(): Boolean {
        updateHandlerAllData()
        if (getAccountValue().isNotBlank() && getPasswordValue().isNotBlank()) {
            if (getAccountValue() == _loginAccount) {
                if (getPasswordValue() == _loginPassword)
                    return true
                else Toast.makeText(
                    mApplication,
                    "e u-o-u, 密码好像输错了呢~",
                    Toast.LENGTH_SHORT
                ).show()
            } else Toast.makeText(mApplication, "这个账号达咩哒~~~", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(mApplication, "请输入账号密码", Toast.LENGTH_SHORT).show()

        return false
    }

    // 逻辑比较复杂一点的点击事件，由于其涉及到的UI data的检查较多, 考虑用双向绑定
    fun userClicked(v: View) {
        when (v.id) {
            R.id.confirm_register -> {
                if (_registerAccount.isNotBlank()) {
                    if (_registerPassword.isNotBlank()) {
                        if (!(_confirmPassword.isNullOrBlank())) {
                            if (saveRegisterData())
                                Toast.makeText(mApplication, "注册成功", Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(
                                    mApplication, "注册失败!请重新注册……", Toast.LENGTH_SHORT
                                ).show()
                        } else Toast.makeText(getApplication(), "密码不一致", Toast.LENGTH_SHORT)
                            .show()
                    } else Toast.makeText(getApplication(), "请设置密码！", Toast.LENGTH_SHORT)
                        .show()
                } else Toast.makeText(getApplication(), "账号不能为空！", Toast.LENGTH_SHORT).show()
            }

            else -> {

            }
        }
    }

    // 清除输入框的数据
    fun clearLiveData() {
        setRegisterAccount("")
        setRegisterPassword("")
        setConfirmPassword("")
    }


    /*
    * 三个getter方法，直接获取 当前handler里保存的三个数据
    * 由于handler初始化之后会确保里面三个key对应位置数据不为空,可以直接非空断言
    * */
    private fun getAccountValue(): String {
        return handle.get<String>(accountKey).toString()
    }

    private fun getPasswordValue(): String {
        return handle.get<String>(passwordKey).toString()
    }

    private fun getIsRememberPasswdValue(): Boolean {
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