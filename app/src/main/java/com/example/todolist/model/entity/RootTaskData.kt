package com.example.todolist.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


/*
* 数据实体
* Entity:
* title 标题
* description 描述
* tag 标签
* priority 优先级
* date: 日期，默认当日
* category: 分类别展示
* Task type: 是否置顶、是否完成、是否常规显示
* parent task（Group task）:
* parent task
*
* 任务编辑栏里面选择各项参数
* 还可以分阶段创建检查事项（类似于 子task）
* */
@Entity
data class RootTaskData(
    var title: String,                  // 标题
    var description: String,            // 描述
    var tag: String,                    // 标签
    var priority: String,               // 优先级
    var date: String,                   // 日期
    var category: String,               // 类别
    var taskType: String,               // 类型
    var taskLevel: String,              // 任务级别
) {
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
}
