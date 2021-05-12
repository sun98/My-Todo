package cn.nibius.mytodo

import android.app.Application

class TasksApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
//    val applicationScope = CoroutineScope(SupervisorJob())
//
//    // Using by lazy so the database and the repository are only created when they're needed
//    // rather than when the application starts
//    val database by lazy { TaskDatabase.getDatabase(this, applicationScope) }
//    val repository by lazy { TaskRepository(database.taskDao()) }
}