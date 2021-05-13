package cn.nibius.mytodo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
data class Task(
    @ColumnInfo(name = "task_title") val taskTitle: String,
    @ColumnInfo(name = "task_status") val taskStatus: Boolean,
    @ColumnInfo(name = "task_detail") val taskDetail: String?,
    @ColumnInfo(name = "task_create_date") val taskCreateDate: Long,
    @ColumnInfo(name = "task_image_url") val taskImageUrl: String = "https://avatars.githubusercontent.com/u/66577",
    @PrimaryKey(autoGenerate = true) val taskId: Long = 0,
    @ColumnInfo(name = "task_ind") val taskInd: Long
)
