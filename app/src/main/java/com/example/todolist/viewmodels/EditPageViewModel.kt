package com.example.todolist.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.todolist.adapters.FoldListAdapter
import com.example.todolist.model.dao.RootTaskDao
import com.example.todolist.model.db.RootTaskDatabase
import com.example.todolist.model.entity.RootTaskData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class EditPageViewModel : ViewModel() {
    private var title: String? = null           // 标题
    private var description: String? = null     // 描述
    private var isFinished: Boolean = false     // 是否完成
    private var setupTime: String? = null       // 设定的目标时间
    private var category: String = "default"    // 类别 不同的list中
    private var parent: String = " this"         // 任务所属的父任务，this表示自己就是父任务
    private val foldData = ArrayList<FoldListAdapter.FoldData>()
    fun saveEditData(poster: RootTaskDao) {
        if (title!!.isBlank() && description!!.isBlank()) {
            return
        }

        val task = RootTaskData()
        if (title!!.isNotBlank()) {
            task.title = title!!
        }
        if (description!!.isNotBlank()) {
            task.description = description!!
        }

        if (isFinished) {
            task.isFinished = true
        }
        setupTime?.let {
            task.setupTime = it
        }

        task.category = category
        task.parent = parent

        poster.insertTask(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Log.d("tag", "保存成功路线")
            }
            .subscribe(object : SingleObserver<Long> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("tag", "订阅成功 ")
                }

                override fun onSuccess(t: Long) {
                    Log.d("tag", "保存成功 id $t")
                }

                override fun onError(e: Throwable) {
                    Log.d("tag", "保存失败 ")
                }
            })
    }

    fun initDatas(context: Context): ArrayList<FoldListAdapter.FoldData> {
        var parentDatas: List<RootTaskData>?
        val poster = RootTaskDatabase.getInstance(context).rootTaskDao()
        val getTasks = poster.getAllParentTask()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // 处理正常情况下的订阅事件
                parentDatas = it
                parentDatas?.apply {
                    foldData.clear()
                    for (i in (this as ArrayList<RootTaskData>)) {
                        val parentBean =
                            FoldListAdapter.FoldData.ParentBean(
                                title = i.title,
                                isFinished = i.isFinished
                            )
                        val children = ArrayList<FoldListAdapter.FoldData.ChildBean>()
                        val getChildrenTask = poster.getChildrenTask(i.title)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ kids ->
                                for (j in kids) {
                                    val childBean =
                                        FoldListAdapter.FoldData.ChildBean(
                                            title = j.title.ifBlank { "无标题子任务" },
                                            isFinished = j.isFinished
                                        )
                                    children.add(childBean)
                                }
                                parentBean.children = children
                            }, {
                            })
                        foldData.add(parentBean)
                    }
                }
            }, {
                // 处理异常情况下的订阅事件
                Log.e("TAG", "onError: ${it.message}")
            })
        return foldData
    }


    fun setTitle(text: String) {
        title = text
    }

    fun setDescription(text: String) {
        description = text
    }

    fun isFinished(value: Boolean) {
        isFinished = value
    }

    fun setupTime(time: String) {
        setupTime = time
    }

    fun setCategory(choice: String) {
        category = choice
    }

    fun setParent(params: String) {
        parent = params
    }
}