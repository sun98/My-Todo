package cn.nibius.mytodo.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table ORDER BY task_status,-taskId")
    fun getAll(): Flow<List<Task>>

//    @Query("SELECT * FROM task_table WHERE taskId IN (:taskIds)")
//    fun loadAllByIds(taskIds: IntArray): List<TaskData>

    @Query("SELECT * FROM task_table WHERE task_title LIKE :keywords OR task_detail LIKE :keywords")
    fun findByTitleAndDetail(keywords: String): List<Task>

    @Insert
    suspend fun insert(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("UPDATE task_table SET task_status=:status WHERE taskId=:taskId")
    suspend fun changeStatus(taskId: Long, status: Boolean)

    @Query("UPDATE task_table SET task_status=:taskStatus,task_title=:taskTitle,task_Detail=:taskDetail,task_create_date=:taskCreateTime WHERE taskId=:taskId")
    suspend fun modify(
        taskId: Long,
        taskTitle: String,
        taskDetail: String?,
        taskStatus: Boolean,
        taskCreateTime: Long
    )
}