package cn.nibius.mytodo

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.nibius.mytodo.activity.AddNewTaskActivity
import cn.nibius.mytodo.room.Task

class TaskAdapter(private val taskViewModel: TaskViewModel) :
    ListAdapter<Task, TaskAdapter.TaskViewHolder>(TASKS_COMPARATOR) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class TaskViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val textTitle: TextView = view.findViewById(R.id.textTaskTitle)
        private val checkBox: CheckBox = view.findViewById(R.id.checkBoxTask)
        private val textDetail: TextView = view.findViewById(R.id.textTaskDetail)
        private val context = view.context

        fun bind(task: Task?, taskViewModel: TaskViewModel) {
            textTitle.text = task?.taskTitle ?: ""
            checkBox.isChecked = task?.taskStatus ?: false
            textDetail.text = task?.taskDetail ?: ""
            checkBox.setOnClickListener {
                taskViewModel.changeStatus(task!!)
            }
            this.view.setOnClickListener {
                val intent = Intent(context, AddNewTaskActivity::class.java)
                intent.putExtra("callAction", "modify")
                intent.putExtra("taskId", task?.taskId)
                intent.putExtra("taskTitle", task?.taskTitle)
                intent.putExtra("taskDetail", task?.taskDetail)
                (context as Activity).startActivityForResult(intent, 1)
            }
        }

        companion object {
            fun create(parent: ViewGroup): TaskViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.task_item, parent, false)
                return TaskViewHolder(view)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.create(viewGroup)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: TaskViewHolder, position: Int) {
        val current = getItem(position)
        viewHolder.bind(current, taskViewModel)
    }

    companion object {
        private val TASKS_COMPARATOR = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.taskId == newItem.taskId
            }
        }
    }
}