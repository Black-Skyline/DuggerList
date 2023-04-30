package com.example.todolist.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.todolist.utils.getViewBinding
import com.example.todolist.utils.BaseBinding

abstract class BaseDataBindingActivity<VB: ViewDataBinding> : AppCompatActivity(), BaseBinding<VB> {
    internal val mBinding :VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        getViewBinding(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(mBinding.root)
        // 子类在initBinding里完成本应在onCreate中完成的工作
        mBinding.initBinding()
    }
}