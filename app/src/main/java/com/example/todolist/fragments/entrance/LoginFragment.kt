package com.example.todolist.fragments.entrance

//import com.example.todolist.utils.actionToActNoParam

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.activities.GateActivity
import com.example.todolist.databinding.FragmentEntranceLoginBinding
import com.example.todolist.utils.BaseTextChangedWatcher
import com.example.todolist.utils.actionToActivityDoSth
import com.example.todolist.viewmodels.AccountDataViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment(), View.OnClickListener {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SavedStateViewModelFactory(requireActivity().application, this)
        )[AccountDataViewModel::class.java]
    }
    // 原来的写法，好像在部分手机(指我的Android 11)上会由于java.lang.reflect.InvocationTargetException而直接闪退
    // 我又看了一下，目前在我的Android 11上运行起来一直闪退，尽管我还是尽量减少涉及到反射的部分代理写法、尽量使用局部变量而不是直接声明为全局交给懒加载或
    // get()、set()方法
//    private var _binding: FragmentEntranceLoginBinding? = null
//    private val mBinding: FragmentEntranceLoginBinding
//        get() = _binding!!
//    _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entrance_login, container, false)


    private var _binding: FragmentEntranceLoginBinding? = null
    private val binding: FragmentEntranceLoginBinding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entrance_login, container, false)
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entrance_login, container, false)
//            FragmentEntranceLoginBinding.inflate(inflater)

        AccountDataViewModel.initInstance(this@LoginFragment.viewModel)
        binding.apply {
            vm = this@LoginFragment.viewModel
            user = viewModel.getDatas()
            lifecycleOwner = this@LoginFragment
            login.setOnClickListener(this@LoginFragment)
            register.setOnClickListener(this@LoginFragment)
            inputText1.isCounterEnabled = false
            inputText1.isCounterEnabled = false
        }

        textChangedListen()
        this@LoginFragment.viewModel.checkIsRememberPasswd()
        return binding.root
    }

//    下面是对DataBinding的封装后在fragment中的简单写法
//    override fun FragmentEntranceLoginBinding.initBinding() {
//        AccountDataViewModel.initInstance(this@LoginFragment.viewModel)
//        mBinding.viewModel = this@LoginFragment.viewModel
//        mBinding.lifecycleOwner = this@LoginFragment
//        mBinding.login.setOnClickListener(this@LoginFragment)
//        mBinding.register.setOnClickListener(this@LoginFragment)
//        mBinding.inputText1.isCounterEnabled = false
//        mBinding.inputText1.isCounterEnabled = false
//        textChangedListen()
//        this@LoginFragment.viewModel.checkIsRememberPasswd()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 假如onDestroyView回调时binding已经调用set方法完成了初始化，就解除绑定
        _binding = null
    }

    private fun textChangedListen() {
        binding.accountBox.addTextChangedListener()
        binding.accountBox.addTextChangedListener(object : BaseTextChangedWatcher() {
            @SuppressLint("UseCompatTextViewDrawableApis", "UseCompatLoadingForDrawables")
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // 当输入框里就不为空时，调用下面的代码段
                if (charSequence.isEmpty() && i2 != 0) {
                    binding.inputText1.isCounterEnabled = true
                    binding.inputText1.isHelperTextEnabled = false
                    viewModel.setDrawableLeft(
                        binding.accountBox,
                        R.color.drawable_highlight_tint
                    )
                    binding.inputText2.isHelperTextEnabled = true
                    binding.inputText2.helperText = getText(R.string.input_passwd_hint)
                }
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() == "") {
                    binding.inputText1.isCounterEnabled = false
                    binding.inputText1.isHelperTextEnabled = true
                    binding.inputText1.helperText = getText(R.string.no_account_hint)
                    viewModel.setDrawableLeft(binding.accountBox, R.color.black)
                }
                else if (editable.toString().length > binding.inputText1.counterMaxLength) {
                    binding.accountBox.error = getString(R.string.user_name_over_length_hint)
                }
            }
        })

        binding.passwordBox.addTextChangedListener(object : BaseTextChangedWatcher() {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // 当输入框里就不为空时，调用下面的代码段
                if (charSequence.isEmpty() && i2 != 0) {
                    binding.inputText2.isCounterEnabled = true
                    binding.inputText2.isHelperTextEnabled = false
                    viewModel.setDrawableLeft(binding.passwordBox, R.color.drawable_highlight_tint)
                    binding.inputText2.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() == "") {
                    binding.inputText2.isCounterEnabled = false
                    binding.inputText2.isHelperTextEnabled = true
                    //                    binding.inputText2.setHelperText(getText(R.string.input_passwd_hint));
                    binding.inputText2.endIconMode = TextInputLayout.END_ICON_NONE
                    if (binding.accountBox.text != null && binding.accountBox.text
                            .toString().isNotEmpty()
                    ) binding.inputText2.helperText = getText(R.string.input_passwd_hint)
                    viewModel.setDrawableLeft(binding.passwordBox, R.color.black)
                }
                else if (editable.toString().length > binding.inputText2.counterMaxLength) {
                    binding.passwordBox.error = getString(R.string.password_over_length_hint)
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login -> {
                if (viewModel.loginCheck()) {
                    viewModel.updateShp(AccountDataViewModel.getIsRememberPasswdKey())
                    val manager = requireActivity().supportFragmentManager
                    manager.findFragmentByTag("登录fragment实例")?.apply {
                        manager.beginTransaction().remove(this).commit()
//                        requireActivity().finish()
                    }
                    actionToActivityDoSth(requireActivity(), GateActivity::class.java) {
                        // 展示过度动画
                    }

                }
            }
            R.id.register -> {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    hide(this@LoginFragment)
                    add(R.id.entrance_fragment_place, RegisterFragment(), "注册fragment实例")
                    commit()
                }
            }
        }
    }
}