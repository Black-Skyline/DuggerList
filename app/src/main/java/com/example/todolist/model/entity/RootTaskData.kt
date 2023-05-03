package com.example.todolist.model.entity

import androidx.room.ColumnInfo
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
    var id: Long = 0,                          // using
    @ColumnInfo(name = "title")
    var title: String = "默认任务",             // 标题   using
    @ColumnInfo(name = "description")
    var description: String = "无描述",        // 描述   using
    @ColumnInfo(name = "tag")
    var tag: String = "无标签",                // 标签 预留后续实现
    @ColumnInfo(name = "priority")
    var priority: Int = 0,                    // 优先级 预留后续实现
    @ColumnInfo(name = "isFinished")
    var isFinished: Boolean = false,          // 是否已完成   using
//    var setupTime: String = "" ,            // 设定的目标时间  预留后续实现
    @ColumnInfo(name = "category")
    var category: String = "default",         // 类别 不同的list中 预留后续实现
    @ColumnInfo(name = "parentId")
    var parent: Long = -1,                    // 父任务id归属, -1代表自己是父任务,其他正数连接到对应id  using
    @ColumnInfo(name = "taskGroup")
    var taskGroup: String = "未分组",          // 任务分组 预留后续实现
)
