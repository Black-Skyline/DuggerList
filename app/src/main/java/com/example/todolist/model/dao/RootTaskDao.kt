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
     * @return May<Int>  返回操作的条数   1 成功  0失败
     */
    @Delete
    fun deleteTask(task: RootTaskData): Maybe<Int>

    /**
     * @return Maybe<Int> 返回删除的条数
     */
    @Query("DELETE FROM RootTasks")
    fun deleteTableAndRun(): Maybe<Int>


    /**
     * 更改一条task 实体
     * @param task: RootTaskData
     * @return Single<Int>  返回操作的条数   1 成功  0失败
     */
    @Update
    fun updateTask(task: RootTaskData): Single<Int>

    @Query("UPDATE RootTasks SET title =:title WHERE id = :id")
    fun updateTaskTitle(id: Long, title: String): Single<Int>

    @Query("UPDATE RootTasks SET description =:description WHERE id = :id")
    fun updateTaskDescription(id: Long, description: String): Single<Int>
    @Query("UPDATE RootTasks SET isFinished =:isFinished WHERE id = :id")
    fun updateTaskDescription(id: Long, isFinished: Boolean): Single<Int>



    /**
     * 查询task 实体
     * @param 自定义
     * @return Single<List<RootTaskData>>  返回一个装着RootTaskData实体的list
     */
    @Query("SELECT * FROM RootTasks WHERE parentId = -1 ORDER BY id DESC")
    fun getAllParentTask(): Single<List<RootTaskData>>


    /**
     * 根据parent获取最后一条
     * parent = -1  父任务的最后一条(最新)
     * parent = num > 0  某个id父任务的最后一条子任务(最新)
     */
    @Query("SELECT * FROM RootTasks WHERE parentId = :parent order by id desc  LIMIT 1")
    fun findLastByParentId(parent: Long): Maybe<RootTaskData>

    @Query("SELECT * FROM RootTasks WHERE parentId = :parent ORDER BY id ASC")
    fun getChildrenTask(parent: Long): Single<List<RootTaskData>>

    @Query("SELECT * FROM RootTasks WHERE id = :id order by id desc  LIMIT 1")
    fun descriptionById(id: Long): Maybe<RootTaskData>

    @get:Query("SELECT * FROM RootTasks")
    val allData: Single<List<RootTaskData>>
}