package cn.nibius.mytodo

import android.util.Log
import androidx.lifecycle.*
import cn.nibius.mytodo.room.Task
import cn.nibius.mytodo.room.TaskRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    val allTasks: LiveData<List<Task>> = repository.allTasks.asLiveData()
    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun changeStatus(task: Task) = viewModelScope.launch {
        repository.changeStatus(task.taskId, !task.taskStatus)
    }

    fun modify(taskId: Long, taskTitle: String, taskDetail: String) = viewModelScope.launch {
        repository.modify(taskId, taskTitle, taskDetail)
    }

}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}