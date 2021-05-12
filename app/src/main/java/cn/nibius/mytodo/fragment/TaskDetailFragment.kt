package cn.nibius.mytodo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.nibius.mytodo.R
import cn.nibius.mytodo.activity.MainActivity
import cn.nibius.mytodo.room.Task
import com.squareup.picasso.Picasso
import java.util.*

class TaskDetailFragment : Fragment(R.layout.fragment_task_detail) {
    var action = ""
    private val picasso = Picasso.get()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        action = bundle?.getString("action") ?: ""
        val mainActivity = (activity as MainActivity)

        val editTitle = view.findViewById<TextView>(R.id.editTaskTitle)
        val editDetail = view.findViewById<TextView>(R.id.editTaskDetail)
        var taskId = 0L
        val btnSaveTask = mainActivity.getFab()
        val imageTaskDetail = view.findViewById<ImageView>(R.id.imageTaskDetail)
        // todo: change the fab icon

        if (action == "editTask") {
            val taskData = bundle!!.getStringArrayList("taskData")
            taskId = (taskData?.get(0) ?: "").toLong()
            editTitle.text = taskData?.get(1) ?: ""
            editDetail.text = taskData?.get(2) ?: ""
            picasso.setIndicatorsEnabled(true)
            if (taskData?.get(3) ?: "" != "") {
                picasso
                    .load(taskData?.get(3) ?: "")
                    .placeholder(R.drawable.ic_baseline_sentiment_satisfied_alt_24)
                    .error(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
                    .into(imageTaskDetail)
            }
        }

        btnSaveTask.setOnClickListener {
            val taskTitle = editTitle.text.toString()
            if (taskTitle == "") {
                Toast.makeText(
                    mainActivity,
                    mainActivity.getString(R.string.task_title_empty),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
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
                        Toast.makeText(
                            mainActivity,
                            mainActivity.getString(R.string.task_added),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    "editTask" -> {
                        val taskTitle = editTitle.text.toString()
                        mainActivity.taskViewModel.modify(
                            taskId,
                            taskTitle,
                            editDetail.text.toString()
                        )
                        Toast.makeText(
                            mainActivity,
                            "Task '${taskTitle}' modified",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                mainActivity.supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).setFabOnClickListener()
        (activity as MainActivity).title = "My Todo"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).title = when (action) {
            "newTask" -> "New Task"
            "editTask" -> "Edit Task"
            else -> "My Todo"
        }
    }
}