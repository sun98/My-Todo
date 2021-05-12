package cn.nibius.mytodo.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.paging.PagingSource

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table ORDER BY task_status,-taskId")
//    fun getAll(): Flow<List<Task>>
    fun getAll(): PagingSource<Int, Task>


//    @Query("SELECT * FROM task_table WHERE taskId IN (:taskIds)")
//    fun loadAllByIds(taskIds: IntArray): List<TaskData>

    @Query("SELECT * FROM task_table WHERE task_title LIKE :keywords OR task_detail LIKE :keywords")
    fun findByTitleAndDetail(keywords: String): List<Task>

    @Insert
    fun insert(task: Task)

    @Insert
    fun insert(tasks: List<Task>)

    @Delete
    fun delete(task: Task)

    @Query("DELETE FROM task_table")
    fun deleteAll()

    @Query("UPDATE task_table SET task_status=:status WHERE taskId=:taskId")
    fun changeStatus(taskId: Long, status: Boolean)

    @Query("UPDATE task_table SET task_title=:taskTitle,task_Detail=:taskDetail WHERE taskId=:taskId")
    fun modify(
        taskId: Long,
        taskTitle: String,
        taskDetail: String?,
    )

//    @Query("DELETE FROM task_table WHERE taskId=:taskId")
//    fun deleteById(taskId: Long)
}