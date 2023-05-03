package com.example.todolist.fragments.gate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.activities.EditTaskActivity
import com.example.todolist.adapters.FoldListAdapter
import com.example.todolist.databinding.FragmentTaskDisplayBinding
import com.example.todolist.fragments.BaseViewBindingFG
import com.example.todolist.model.db.RootTaskDatabase
import com.example.todolist.model.entity.RootTaskData
import com.example.todolist.viewmodels.GateViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class TaskDisplayFragment :
    BaseViewBindingFG<FragmentTaskDisplayBinding>(FragmentTaskDisplayBinding::inflate) {
    private val viewModel by lazy { ViewModelProvider(this)[GateViewModel::class.java] }
    private val addMore: FloatingActionButton get() = binding.fabAddMore

    private val foldData = ArrayList<FoldListAdapter.FoldData>()
    private val adapterInstance = FoldListAdapter(foldData)

    // 注册一个ActivityFinishedForResult
    private var launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            // to do
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatas()
        addMore.setOnClickListener {
            launcher.launch(Intent(this.context, EditTaskActivity::class.java))
        }
        binding.taskDisplayList.adapter = adapterInstance
    }


    private fun initDatas() {

        var parentDatas: List<RootTaskData>?
        val poster = RootTaskDatabase.getInstance(requireActivity()).rootTaskDao()
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
                                title = i.title.ifBlank { "无标题任务" },
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
                adapterInstance.setNewDatas(foldData)
            }, {
                // 处理异常情况下的订阅事件
                Log.e("TAG", "onError: ${it.message}")
            })


    }
}