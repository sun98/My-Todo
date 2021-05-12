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

    @Query("DELETE FROM task_table WHERE taskId=:taskId")
    fun deleteById(taskId: Long)

    @Query("UPDATE task_table SET task_title=(CASE WHEN taskId=:oldId then :newTitle ELSE :oldTitle END),task_detail=(CASE WHEN taskId=:oldId then :newDetail ELSE :oldDetail END),task_image_url=(CASE WHEN taskId=:oldId then :newImage ELSE :oldImage END),task_status=(CASE WHEN taskId=:oldId then :newStatus ELSE :oldStatus END),task_create_date=(CASE WHEN taskId=:oldId then :newDate ELSE :oldDate END) WHERE taskId IN(:oldId, :newId)")
    fun swap(
        oldId: Long,
        newId: Long,
        oldTitle: String,
        newTitle: String,
        oldDetail: String,
        newDetail: String,
        oldStatus: Boolean,
        newStatus: Boolean,
        oldImage: String,
        newImage: String,
        oldDate: Long,
        newDate: Long
    )
}

//update t
//set t.id = (case when t.id = 100 then 101 else 101 end)
//where t.id in (100, 101)

//UPDATE t, t as t2
//SET t.id = t2.id, t2.id = t.id
//WHERE t.id = 1 AND t2.id = 2