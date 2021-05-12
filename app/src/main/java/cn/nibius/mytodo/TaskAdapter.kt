package cn.nibius.mytodo

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.nibius.mytodo.activity.MainActivity
import cn.nibius.mytodo.fragment.TaskDetailFragment
import cn.nibius.mytodo.room.Task
import com.squareup.picasso.Picasso

class TaskAdapter(private val taskViewModel: TaskViewModel) :
    PagingDataAdapter<Task, TaskAdapter.TaskViewHolder>(TASKS_COMPARATOR) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val TAG = "task view holder"
        var task: Task? = null
            private set
        private val textTitle: TextView = view.findViewById(R.id.textTaskTitle)
        private val checkBox: CheckBox = view.findViewById(R.id.checkBoxTask)
        private val textDetail: TextView = view.findViewById(R.id.textTaskDetail)
        private val taskImage: ImageView = view.findViewById(R.id.imageTask)
        private val taskText: ConstraintLayout = view.findViewById(R.id.layoutTaskText)
        private val context = view.context
        private val picasso = Picasso.get()

        fun bind(task: Task?, taskViewModel: TaskViewModel) {
            this.task = task
            textTitle.text = task?.taskTitle ?: ""
            textTitle.setTypeface(null, Typeface.BOLD)
            checkBox.isChecked = task?.taskStatus ?: false
            textDetail.text = task?.taskDetail ?: ""
            Log.d(TAG, "bind: ${task?.taskImageUrl}")
            picasso.setIndicatorsEnabled(true)
            picasso
                .load(task?.taskImageUrl)
                .into(taskImage)
            checkBox.setOnClickListener {
                taskViewModel.changeStatus(task!!)
            }
            taskText.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("action", "editTask")
                bundle.putStringArrayList(
                    "taskData",
                    arrayListOf(
                        task?.taskId.toString(),
                        task?.taskTitle,
                        task?.taskDetail,
                        task?.taskImageUrl
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
            taskImage.setOnClickListener {
                val dialog =
                    Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.image_dialog)
                val bigImage = dialog.findViewById<ImageView>(R.id.imageDialog)
                picasso.setIndicatorsEnabled(true)
                picasso
                    .load(task?.taskImageUrl)
                    .placeholder(R.drawable.ic_baseline_sentiment_satisfied_alt_24)
                    .error(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
                    .into(bigImage)
                bigImage.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }

        companion object {
            fun create(parent: ViewGroup): TaskViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.task_item,
                        parent,
                        false
                    )
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
//        if (viewHolder is TaskViewHolder) {
        viewHolder.bind(getItem(position), taskViewModel)
//        }
    }

    companion object {
        private val TASKS_COMPARATOR = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.taskId == newItem.taskId
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }

}