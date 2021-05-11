package cn.nibius.mytodo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey val taskId: Long,
    @ColumnInfo(name = "task_title") val taskTitle: String,
    @ColumnInfo(name = "task_status") val taskStatus: Boolean,
    @ColumnInfo(name = "task_detail") val taskDetail: String?,
    @ColumnInfo(name = "task_create_date") val taskCreateDate: Long
)
