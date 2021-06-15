package cn.nibius.mytodo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.nibius.mytodo.util.ioThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors

/**
 * 单例模式
 */
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(
            context: Context
        ): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            fillInDb(context.applicationContext)
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun fillInDb(context: Context) {
            ioThread {
                getDatabase(context).taskDao().insert((1..200).map {
                    Task(
                        "Task $it",
                        false,
                        "Detail of task $it",
                        Calendar.getInstance().timeInMillis,
                        "https://avatars.githubusercontent.com/u/66577",
                        0,
                        it.toLong()
                    )
                })
            }
        }
    }
}