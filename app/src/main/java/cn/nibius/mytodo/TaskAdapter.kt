package cn.nibius.mytodo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.fragment.app.add
import androidx.fragment.app.replace
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.nibius.mytodo.activity.AddNewTaskActivity
import cn.nibius.mytodo.activity.MainActivity
import cn.nibius.mytodo.fragment.TaskDetailFragment
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
                val bundle = Bundle()
                bundle.putString("action", "editTask")
                bundle.putStringArrayList(
                    "taskData",
                    arrayListOf(
                        task?.taskId.toString(),
                        task?.taskTitle,
                        task?.taskDetail
                    )
                )
                (context as MainActivity).supportFragmentManager.commit {
                    this.setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                    )
                    replace<TaskDetailFragment>(R.id.mainFragmentContainerView, args = bundle)
                    setReorderingAllowed(true)
                    addToBackStack("editTask")
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup, viewType: Int): TaskViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(
                        if (viewType == 0) R.layout.task_item else R.layout.progress_bar,
                        parent,
                        false
                    )
                return TaskViewHolder(view)
            }
        }
    }

    class ProgressViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.create(viewGroup, viewType)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: TaskViewHolder, position: Int) {
//        if(viewHolder is TaskViewHolder){
        val current = getItem(position)
        viewHolder.bind(current, taskViewModel)
//        } else if(viewHolder is ProgressViewHolder){
//
//        }

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

//    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
//    }
}