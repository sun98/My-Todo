package cn.nibius.mytodo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.nibius.mytodo.R
import cn.nibius.mytodo.activity.MainActivity
import cn.nibius.mytodo.room.Task
import java.util.*

class TaskDetailFragment : Fragment(R.layout.fragment_task_detail) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val action = bundle?.getString("action")
        val mainActivity = (activity as MainActivity)

        val editTitle = view.findViewById<TextView>(R.id.editTaskTitle)
        val editDetail = view.findViewById<TextView>(R.id.editTaskDetail)
        var taskId = 0L
        val btnSaveTask = mainActivity.getFab()
        // todo: change the fab icon

        if (action == "editTask") {
            val taskData = bundle.getStringArrayList("taskData")
            taskId = (taskData?.get(0) ?: "").toLong()
            editTitle.text = taskData?.get(1) ?: ""
            editDetail.text = taskData?.get(2) ?: ""
        }

        btnSaveTask.setOnClickListener {
            val taskTitle = editTitle.text.toString()
            if (taskTitle == "") {
                Toast.makeText(
                    context,
                    "Task title can not be empty.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            when (action) {
                "newTask" -> {
                    val curTime = Calendar.getInstance().timeInMillis
                    mainActivity.taskViewModel.insert(
                        Task(
                            taskTitle = editTitle.text.toString(),
                            taskStatus = false,
                            taskDetail = editDetail.text.toString(),
                            taskCreateDate = curTime
                        )
                    )
                    Toast.makeText(context, getString(R.string.task_added), Toast.LENGTH_SHORT)
                        .show()
                }
                "editTask" -> {
                    val taskTitle = editTitle.text.toString()
                    mainActivity.taskViewModel.modify(
                        taskId,
                        taskTitle,
                        editDetail.text.toString()
                    )
                    Toast.makeText(context, "Task '${taskTitle}' modified", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            mainActivity.supportFragmentManager.popBackStack()
        }
    }
}