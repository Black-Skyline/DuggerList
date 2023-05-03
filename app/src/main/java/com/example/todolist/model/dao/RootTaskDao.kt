package com.example.todolist.model.dao

import androidx.room.*
import com.example.todolist.model.entity.RootTaskData
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface RootTaskDao {

    /**
     * 插入一条task 实体
     * @param task: RootTaskData
     * @return Single<Long>  返回插入的主键id
     */
    @Insert
    fun insertTask(task: RootTaskData): Single<Long>

    @Insert
    fun insertTaskS(vararg tasks: RootTaskData): Single<List<Long>>


    /**
     * 删除一条task 实体
     * @param task: RootTaskData
     * @return Single<Long>  返回删除的主键id
     */
    @Delete
    fun deleteTask(task: RootTaskData): Maybe<Int>

    @Query("DELETE FROM RootTasks")
    fun deleteTableAndRun()


    /**
     * 更改一条task 实体
     * @param task: RootTaskData
     * @return Single<Long>  返回更改的主键id
     */
    @Update
    fun updateTask(task: RootTaskData): Single<Int>

    @Query("UPDATE RootTasks SET title = :title WHERE id = :id")
    fun updateTaskTitle(id: Long, title: String): Single<Int>

    @Query("UPDATE RootTasks SET description = :description WHERE id = :id")
    fun updateTaskDescription(id: Long, description: String): Single<Int>



    /**
     * 查询task 实体
     * @param 自定义
     * @return Single<List<RootTaskData>>  返回一个装着RootTaskData实体的list
     */
    @Query("SELECT * FROM RootTasks WHERE parent LIKE ' this' ORDER BY id DESC")
    fun getAllParentTask(): Single<List<RootTaskData>>

    @Query("SELECT * FROM RootTasks WHERE parent LIKE :parentTitle ORDER BY id ASC")
    fun getChildrenTask(parentTitle: String): Single<List<RootTaskData>>

    @get:Query("SELECT * FROM RootTasks")
    val allData: Single<List<RootTaskData>>
}