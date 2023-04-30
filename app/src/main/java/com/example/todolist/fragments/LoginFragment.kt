package com.example.todolist.fragments

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
import com.example.todolist.activities.EntranceActivity
import com.example.todolist.activities.GateActivity
import com.example.todolist.databinding.FragmentEntranceLoginBinding
import com.example.todolist.utils.BaseTextChangedWatcher
import com.example.todolist.utils.actionToActNoParam

import com.example.todolist.viewmodels.AccountDataViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment(), View.OnClickListener {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SavedStateViewModelFactory(requireActivity().application, this)
        )[AccountDataViewModel::class.java]
    }
    private lateinit var _binding: FragmentEntranceLoginBinding
    private val mBinding: FragmentEntranceLoginBinding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entrance_login, container, false)
            AccountDataViewModel.initInstance(this@LoginFragment.viewModel)
        mBinding.viewModel = this@LoginFragment.viewModel
        mBinding.lifecycleOwner = this@LoginFragment
        mBinding.login.setOnClickListener(this@LoginFragment)
        mBinding.register.setOnClickListener(this@LoginFragment)
        mBinding.inputText1.isCounterEnabled = false
        mBinding.inputText1.isCounterEnabled = false
        textChangedListen()
        this@LoginFragment.viewModel.checkIsRememberPasswd()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        AccountDataViewModel.initInstance(viewModel)

    }

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

    private fun textChangedListen() {
        mBinding.accountBox.addTextChangedListener()
        mBinding.accountBox.addTextChangedListener(object : BaseTextChangedWatcher() {
            @SuppressLint("UseCompatTextViewDrawableApis", "UseCompatLoadingForDrawables")
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // 当输入框里就不为空时，调用下面的代码段
                if (charSequence.isEmpty() && i2 != 0) {
                    mBinding.inputText1.isCounterEnabled = true
                    mBinding.inputText1.isHelperTextEnabled = false
                    viewModel.setDrawableLeft(
                        mBinding.accountBox,
                        R.color.drawable_highlight_tint
                    )
                    mBinding.inputText2.isHelperTextEnabled = true
                    mBinding.inputText2.helperText = getText(R.string.input_passwd_hint)
                }
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() == "") {
                    mBinding.inputText1.isCounterEnabled = false
                    mBinding.inputText1.isHelperTextEnabled = true
                    mBinding.inputText1.helperText = getText(R.string.no_account_hint)
                    viewModel.setDrawableLeft(mBinding.accountBox, R.color.black)
                } else if (editable.toString().length > mBinding.inputText1.counterMaxLength) {
                    mBinding.accountBox.error = getString(R.string.user_name_over_length_hint)
                }
            }
        })

        mBinding.passwordBox.addTextChangedListener(object : BaseTextChangedWatcher() {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // 当输入框里就不为空时，调用下面的代码段
                if (charSequence.isEmpty() && i2 != 0) {
                    mBinding.inputText2.isCounterEnabled = true
                    mBinding.inputText2.isHelperTextEnabled = false
                    viewModel.setDrawableLeft(mBinding.passwordBox, R.color.drawable_highlight_tint)
                    mBinding.inputText2.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() == "") {
                    mBinding.inputText2.isCounterEnabled = false
                    mBinding.inputText2.isHelperTextEnabled = true
                    //                    mBinding.inputText2.setHelperText(getText(R.string.input_passwd_hint));
                    mBinding.inputText2.endIconMode = TextInputLayout.END_ICON_NONE
                    if (mBinding.accountBox.text != null && mBinding.accountBox.text
                            .toString().isNotEmpty()
                    ) mBinding.inputText2.helperText = getText(R.string.input_passwd_hint)
                    viewModel.setDrawableLeft(mBinding.passwordBox, R.color.black)
                } else if (editable.toString().length > mBinding.inputText2.counterMaxLength) {
                    mBinding.passwordBox.error = getString(R.string.password_over_length_hint)
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login -> {
                if (viewModel.loginCheck()) {
                    viewModel.updateShp(AccountDataViewModel.getIsRememberPasswdKey())
                    actionToActNoParam(requireActivity(), GateActivity::class.java)
                }
            }
            R.id.register -> {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    hide(this@LoginFragment)
                    add(R.id.entrance_fragment_place, RegisterFragment(), "注册fragment实例")
                    addToBackStack(EntranceActivity.BACK_STACK_ROOT_TAG)
                    commit()
                }
            }
        }
    }
}