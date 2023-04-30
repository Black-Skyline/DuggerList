package com.example.todolist.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.todolist.utils.getViewBinding
import com.example.todolist.utils.BaseBinding

abstract class BaseDataBindingFG<VB : ViewDataBinding>: Fragment(), BaseBinding<VB> {
    protected lateinit var mBinding: VB
    private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = getViewBinding(inflater, container,false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 子类在initBinding里完成本应在onViewCreate中完成的工作
        mBinding.initBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 假如onDestroyView回调时mBinding已经调用set方法完成了初始化，就解除绑定
        if(::mBinding.isInitialized){
            mBinding.unbind()
        }
    }
    companion object {
        fun <T : Activity> actionToActNoParam(context: Context, destination: Class<T>) {
            val intent = Intent(context, destination)
            context.startActivity(intent)
        }
    }

}