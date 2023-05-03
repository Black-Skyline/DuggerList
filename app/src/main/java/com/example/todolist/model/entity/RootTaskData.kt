package com.example.todolist.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.ExperimentalTime


/*
* 数据实体
* Entity:
* title 标题
* description 描述
* tag 标签
* priority 优先级
* date: 日期，默认当日
* category: 分类别展示
* TaskType: 是否置顶、是否完成、是否常规显示
* parent task（Group task）
* child task
*
* 任务编辑栏里面选择各项参数
* 还可以分阶段创建检查事项（类似于 子task）
* */
@Entity(tableName = "RootTasks")
data class RootTaskData(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    var title: String = "",             // 标题
    var description: String = "",       // 描述
    var tag: String? = null,            // 标签 预留后续实现
    var priority: String? = null,       // 优先级 预留后续实现
    var isFinished: Boolean = false,    // 是否完成
    var setupTime: String? = null ,     // 设定的目标时间
    var category: String = "default",   // 类别 不同的list中
    var parent: String = " this",       // 任务所属的父任务, " this"表示自己就是父任务
    var taskGroup: String? = null,      // 任务分组 预留后续实现
)
