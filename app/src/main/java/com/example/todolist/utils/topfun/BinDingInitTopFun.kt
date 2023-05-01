package com.example.todolist.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import java.lang.reflect.ParameterizedType

// 在Activity使用反射调用inflate方法完成DataBinding
inline fun <VB : ViewDataBinding> Activity.getViewBinding(inflater: LayoutInflater): VB {
    val vbClass =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VB>>()
    val inflate = vbClass[0].getDeclaredMethod("inflate", LayoutInflater::class.java)
    return inflate.invoke(null, inflater) as VB
}

// 在Fragment使用反射调用inflate方法完成DataBinding
inline fun <VB : ViewDataBinding> Fragment.getViewBinding(
    inflater: LayoutInflater,
    container: ViewGroup?,
    attachToRoot: Boolean
): VB {
    val vbClass =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VB>>()
    val inflate = vbClass[0].getDeclaredMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    return inflate.invoke(null, inflater, container, attachToRoot) as VB
}

// 有考虑在Fragment使用反射调用setContentView（getActivity(), LayoutResID）方法完成DataBinding, 原理同上


// 从fragment到Activity的无参跳转
fun <T : Activity> Fragment.actionToActivityNoParam(context: Context, destination: Class<T>) {
    val intent = Intent(context, destination)
    context.startActivity(intent)
}

// 从fragment到Activity的无参跳转
fun <T : Activity> Fragment.actionToActivityDoSth(context: Context, destination: Class<T>, beforeAction: () -> Unit) {
    val intent = Intent(context, destination)
    beforeAction()
    context.startActivity(intent)
}