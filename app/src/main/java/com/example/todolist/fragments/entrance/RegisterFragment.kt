package com.example.todolist.fragments.entrance

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.databinding.FragmentEntranceRegisterBinding
import com.example.todolist.utils.BaseTextChangedWatcher
import com.example.todolist.viewmodels.AccountDataViewModel
import com.google.android.material.textfield.TextInputLayout

class RegisterFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SavedStateViewModelFactory(requireActivity().application, this)
        )[AccountDataViewModel::class.java]
    }
    private lateinit var _binding: FragmentEntranceRegisterBinding
    private val mBinding: FragmentEntranceRegisterBinding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_entrance_register, container, false)
        AccountDataViewModel.initInstance(viewModel)
        mBinding.apply {
            vm = viewModel
            user = viewModel.getDatas()
            lifecycleOwner = this@RegisterFragment
            mBinding.backLoginPage.setOnClickListener {
                this@RegisterFragment.viewModel.let {
                    it.clearLiveData()
//                    it.updateHandlerAllData()
                }
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()

                transaction.apply {
                    remove(this@RegisterFragment)
                    manager.findFragmentByTag("登录fragment实例")?.let { it1 -> show(it1) }
                    commit()
                }
            }
        }
        textChangedListen()
        return mBinding.root
    }

//    下面是对DataBinding的封装后在fragment中的简单写法
//    override fun FragmentEntranceRegisterBinding.initBinding() {
//        AccountDataViewModel.initInstance(this@RegisterFragment.viewModel)
//        mBinding.viewModel = this@RegisterFragment.viewModel
//        mBinding.backLoginPage.setOnClickListener {
//            this@RegisterFragment.viewModel.clearLiveData()
//            val manager = requireActivity().supportFragmentManager
//            val transaction = manager.beginTransaction()
//
//            transaction.apply {
//                remove(this@RegisterFragment)
//            }
//            val last = manager.findFragmentByTag("登录fragment实例")?.apply {
//                transaction.show(this@apply)
//            }
//            transaction.apply {
//                addToBackStack(EntranceActivity.BACK_STACK_ROOT_TAG)
//                commit()
//            }
//        }
//        mBinding.lifecycleOwner = this@RegisterFragment
//        textChangedListen()
//    }

    private fun textChangedListen() {
        mBinding.regisAccountBox.addTextChangedListener(object : BaseTextChangedWatcher() {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                super.beforeTextChanged(charSequence, i, i1, i2)
                // 当输入框里就不为空时，调用下面的代码段
                if (charSequence.isEmpty() && i2 != 0) {
                    mBinding.regisText1.isCounterEnabled = true
                    mBinding.regisText1.isHelperTextEnabled = false
                    viewModel.setDrawableLeft(
                        mBinding.regisAccountBox,
                        R.color.drawable_highlight_tint
                    )
                    mBinding.regisText2.isHelperTextEnabled = true
                    mBinding.regisText2.helperText = getText(R.string.regis_passwd_hint)
                }
            }

            override fun afterTextChanged(editable: Editable) {
                super.afterTextChanged(editable)
                if (editable.toString() == "") {
                    mBinding.regisText1.isCounterEnabled = false
                    mBinding.regisText1.isHelperTextEnabled = true
                    mBinding.regisText1.helperText = getText(R.string.regis_account_hint)
                    viewModel.setDrawableLeft(mBinding.regisAccountBox, R.color.black)
                } else if (editable.toString().length > mBinding.regisText1.counterMaxLength) {
                    mBinding.regisAccountBox.error = getString(R.string.user_name_over_length_hint)
                }
            }
        })

        mBinding.regisPasswdBox.addTextChangedListener(object : BaseTextChangedWatcher() {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                super.beforeTextChanged(charSequence, i, i1, i2)
                // 当输入框里就不为空时，调用下面的代码段
                if (charSequence.isEmpty() && i2 != 0) {
                    mBinding.regisText2.isCounterEnabled = true
                    mBinding.regisText2.isHelperTextEnabled = false
                    viewModel.setDrawableLeft(
                        mBinding.regisPasswdBox,
                        R.color.drawable_highlight_tint
                    )
                    mBinding.regisText2.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                }
            }

            override fun afterTextChanged(editable: Editable) {
                super.afterTextChanged(editable)
                if (editable.toString() == "") {
                    mBinding.regisText2.isCounterEnabled = false
                    mBinding.regisText2.isHelperTextEnabled = true
                    //                    mBinding.regisText2.setHelperText(getText(R.string.regis_passwd_hint));
                    mBinding.regisText2.endIconMode = TextInputLayout.END_ICON_NONE
                    if (mBinding.regisAccountBox.text != null &&
                        mBinding.regisAccountBox.text.toString().isNotEmpty()
                    ) mBinding.regisText2.helperText = getText(R.string.regis_passwd_hint)
                    viewModel.setDrawableLeft(mBinding.regisPasswdBox, R.color.black)
                } else if (editable.toString().length > mBinding.regisText2.counterMaxLength) {
                    mBinding.regisPasswdBox.error = getString(R.string.password_over_length_hint)
                }
            }
        })

        mBinding.confirmPasswdBox.addTextChangedListener(object : BaseTextChangedWatcher() {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                super.beforeTextChanged(charSequence, i, i1, i2)
                // 当输入框里就不为空时，调用下面的代码段
                if (charSequence.isEmpty() && i2 != 0) {
                    mBinding.regisText3.isCounterEnabled = true
                    mBinding.regisText3.isHelperTextEnabled = false
                    viewModel.setDrawableLeft(
                        mBinding.confirmPasswdBox,
                        R.color.drawable_highlight_tint
                    )
                    mBinding.regisText3.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                }
            }

            override fun afterTextChanged(editable: Editable) {
                super.afterTextChanged(editable)
                if (editable.toString() == "") {
                    mBinding.regisText3.isCounterEnabled = false
                    mBinding.regisText3.isHelperTextEnabled = true
                    //                    mBinding.regisText3.setHelperText(getText(R.string.confirm_passwd_hint));
                    mBinding.regisText3.endIconMode = TextInputLayout.END_ICON_NONE
                    if (mBinding.regisAccountBox.text != null && mBinding.regisAccountBox.text
                            .toString().isNotEmpty()
                    ) if (mBinding.regisPasswdBox.text != null && mBinding.regisPasswdBox.text
                            .toString().isNotEmpty()
                    ) mBinding.regisText2.helperText = getText(R.string.confirm_passwd_hint)
                    viewModel.setDrawableLeft(mBinding.confirmPasswdBox, R.color.black)
                } else if (editable.toString().length > mBinding.regisText2.counterMaxLength) {
                    mBinding.regisPasswdBox.error = getString(R.string.password_over_length_hint)
                }
            }
        })
    }

}