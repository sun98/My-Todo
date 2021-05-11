package cn.nibius.mytodo.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cn.nibius.mytodo.activity.MainActivity
import cn.nibius.mytodo.R
import cn.nibius.mytodo.TaskAdapter

class TaskListFragment : Fragment(R.layout.fragment_task_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskRecyclerView: RecyclerView = view.findViewById(R.id.recTasks)

        val taskViewModel = (activity as MainActivity).taskViewModel
        val taskAdapter = TaskAdapter(taskViewModel)
        taskRecyclerView.adapter = taskAdapter

        taskViewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            tasks.let { taskAdapter.submitList(it) }
        }

    }

}