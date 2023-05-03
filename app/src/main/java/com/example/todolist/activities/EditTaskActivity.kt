package com.example.todolist.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.databinding.ActivityEditTaskBinding
import com.example.todolist.model.dao.RootTaskDao
import com.example.todolist.model.db.RootTaskDatabase
import com.example.todolist.model.entity.RootTaskData
import com.example.todolist.viewmodels.EditPageViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.MaybeObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class EditTaskActivity : BaseActivity() {

    private val binding by lazy { ActivityEditTaskBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[EditPageViewModel::class.java] }

    // 标题   using
    private val titleBox: EditText get() = binding.editTaskTitle
    private var title: String = "默认任务"

    // 描述   using
    private val contentBox: EditText get() = binding.editTaskDescription

    // 是否已完成   using
    private val isFinishedBox: CheckBox get() = binding.checkBox
    private val backGate: Button get() = binding.button
    private lateinit var poster: RootTaskDao
    //    // 设定的目标时间  预留后续实现
    //    private val setupTime: TextView get() = binding.editCurrentTime
    //    // 类别 不同的list中 预留后续实现
    //    private val currentList: TextView get() = binding.editPageCurrentList
    private var mode = "create"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        poster = RootTaskDatabase.getInstance(this).rootTaskDao()

        initView()
        intent.apply {
            if (getStringExtra("mode") == "change") {
                mode = "change"
                val id = getLongExtra("id", -1)
                if (id.toInt() != -1) {
                    viewModel.setId(id)
                    getDescriptionById(id)
                }

                viewModel.setParent(getLongExtra("parent", -1))
                title = getStringExtra("title") as String
                titleBox.setText(title)
                isFinishedBox.isChecked = getBooleanExtra("isFinished", false)
            }
        }
    }

    private fun initView() {
        isFinishedBox.setOnCheckedChangeListener { _, b ->
            viewModel.isFinished(b)
        }
        // 点击返回按钮时保存数据到数据库
        backGate.setOnClickListener {
            viewModel.setTitle(titleBox.text.toString())
//            Log.d("TAG", "title: ${titleBox.text.toString()}")
            viewModel.setDescription(contentBox.text.toString())
//            Log.d("TAG", "content: ${contentBox.text.toString()}")
            if (mode == "change") {
                viewModel.updateTaskData(poster)
            } else {
                viewModel.saveEditData(poster)
            }

//            viewModel.setupTime()
//            viewModel.setParent()
//            viewModel.setCategory()
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.setTitle(titleBox.text.toString())
        viewModel.setDescription(contentBox.text.toString())

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mode == "change") {
            viewModel.updateTaskData(poster)
        } else {
            viewModel.saveEditData(poster)
        }
        setResult(RESULT_OK)
    }

    private fun getDescriptionById(id: Long) {
        poster.descriptionById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<RootTaskData> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                    // 没查到，表示id有问题
                }

                override fun onSuccess(t: RootTaskData) {
                    if (t.description.isNotBlank()) {
                        contentBox.setText(t.description)
                    }
                }
            })
    }
}