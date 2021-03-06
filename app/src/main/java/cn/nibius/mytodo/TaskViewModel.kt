package cn.nibius.mytodo

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import cn.nibius.mytodo.room.Task
import cn.nibius.mytodo.room.TaskDao
import cn.nibius.mytodo.room.TaskDatabase
import cn.nibius.mytodo.util.ioThread
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalArgumentException

class TaskViewModel(private val dao: TaskDao) : ViewModel() {
    //    val allTasks: LiveData<List<Task>> = repository.allTasks.asLiveData()
    val taskFlow: Flow<PagingData<Task>> = Pager(
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            maxSize = 100
        )
    ) {
        dao.getAll()
    }.flow.cachedIn(viewModelScope)

    fun insert(task: Task) = ioThread {
        dao.insert(task)
    }

    fun changeStatus(task: Task) = ioThread {
        dao.changeStatus(task.taskId, !task.taskStatus)
    }

    fun modify(taskId: Long, taskTitle: String, taskDetail: String) = ioThread {
        dao.modify(taskId, taskTitle, taskDetail)
    }

    fun delete(task: Task) = ioThread {
        dao.delete(task)
    }

    val taskMaxInd: LiveData<Long> = dao.getSize()

    fun reOrder(taskId: Long, newInd: Long) = ioThread {
        dao.reOrder(taskId, newInd)
    }

    fun move(taskIndBegin: Long, taskIndEnd: Long, step: Long) = ioThread {
        Log.d("task view model", "move: ")
        dao.move(taskIndBegin, taskIndEnd, step)
    }
}

class TaskViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(TaskDatabase.getDatabase(app).taskDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}