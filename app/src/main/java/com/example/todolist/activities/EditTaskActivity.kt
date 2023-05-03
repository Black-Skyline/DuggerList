package com.example.todolist.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.databinding.ActivityEditTaskBinding
import com.example.todolist.model.db.RootTaskDatabase
import com.example.todolist.viewmodels.EditPageViewModel

class EditTaskActivity : BaseActivity() {

    private val binding by lazy { ActivityEditTaskBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[EditPageViewModel::class.java] }

    private val titleBox: EditText get() = binding.editTaskTitle
    private val contentBox: EditText get() = binding.editTaskDescription
    private val isFinishedBox: CheckBox get() = binding.checkBox
    private val backGate: Button get() = binding.button
    private val setupTime: TextView get() = binding.editCurrentTime
    private val currentList: TextView get() = binding.editPageCurrentList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        isFinishedBox.setOnCheckedChangeListener { _, b ->
            viewModel.isFinished(b)
        }
        backGate.setOnClickListener {
            viewModel.setTitle(titleBox.text.toString())
            Log.d("TAG", "title: ${titleBox.text.toString()}")
            viewModel.setDescription(contentBox.text.toString())
            Log.d("TAG", "content: ${contentBox.text}")
            viewModel.saveEditData(RootTaskDatabase.getInstance(this).rootTaskDao())
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
        viewModel.saveEditData(RootTaskDatabase.getInstance(this).rootTaskDao())
    }

}