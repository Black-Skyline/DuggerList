package com.example.todolist.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.room.Delete
import com.example.todolist.adapters.FoldListAdapter
import com.example.todolist.model.dao.RootTaskDao
import com.example.todolist.model.db.RootTaskDatabase
import com.example.todolist.model.entity.RootTaskData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class EditPageViewModel : ViewModel() {
    private var id: Long = -1
    private var title: String = "默认任务"         // 标题
    private var description: String = "无描述"    // 描述
    private var isFinished: Boolean = false      // 是否完成

    //    private var setupTime: String = null       // 设定的目标时间
    private var category: String = "default"     // 类别 不同的list中
    private var parent: Long = -1                // 任务所属的父任务，this表示自己就是父任务
    private val foldData = ArrayList<FoldListAdapter.FoldData>()
    fun saveEditData(poster: RootTaskDao) {
        if (title.isBlank() && description.isBlank()) {
            return
        }
        val task = RootTaskData()
        if (title.isNotBlank()) {
            task.title = title
        }
        if (description.isNotBlank()) {
            task.description = description
        }
        task.parent = parent
        task.isFinished = isFinished

//        setupTime?.let {
//            task.setupTime = it
//        }
//        task.category = category

        poster.insertTask(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Log.d("tag", "Next保存成功")
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

    fun deleteTask(poster: RootTaskDao) {
        if (id.toInt() == -1) {
            return
        }
        val delete = poster.deleteById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("tag", "deleteTask: delete成功")
            }, {
                Log.d("tag", "deleteTask: delete失败")
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
                                isFinished = i.isFinished,
                                id = i.id
                            )
                        val children = ArrayList<FoldListAdapter.FoldData.ChildBean>()
                        val getChildrenTask = poster.getChildrenTask(i.id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ kids ->
                                for (j in kids) {
                                    val childBean =
                                        FoldListAdapter.FoldData.ChildBean(
                                            title = j.title.ifBlank { "无标题子任务" },
                                            isFinished = j.isFinished,
                                            id = j.id,
                                            parent = j.parent
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

    fun setId(id: Long) {
        this.id = id
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

//    fun setupTime(time: String) {
//        setupTime = time
//    }

    fun setCategory(choice: String) {
        category = choice
    }

    fun setParent(params: Long) {
        parent = params
    }

    fun updateTaskData(poster: RootTaskDao) {
        if (id.toInt() != -1) {
            if (title.isBlank() && description.isBlank()) {
                deleteTask(poster)
                return
            }
            val task = RootTaskData(id, isFinished = isFinished, parent = parent)
            if (title.isNotBlank()) {
                task.title = title
            }
            if (description.isNotBlank()) {
                task.description = description
            }

//        setupTime?.let {
//            task.setupTime = it
//        }
//            task.category = category
            val update = poster.updateTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("tag", "updateTaskData: update成功")
                }, {
                    Log.d("tag", "updateTaskData: update失败")
                })
        }
    }
}