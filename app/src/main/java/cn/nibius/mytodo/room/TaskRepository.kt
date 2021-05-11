package cn.nibius.mytodo.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allTasks: Flow<List<Task>> = taskDao.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun changeStatus(taskId: Long, status: Boolean) {
        taskDao.changeStatus(taskId, status)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun modify(taskId: Long, taskTitle: String, taskDetail: String) {
        taskDao.modify(taskId, taskTitle, taskDetail)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteById(taskId: Long) {
        taskDao.deleteById(taskId)
    }
}