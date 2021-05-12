package cn.nibius.mytodo.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cn.nibius.mytodo.activity.MainActivity
import cn.nibius.mytodo.R
import cn.nibius.mytodo.TaskAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskListFragment : Fragment(R.layout.fragment_task_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskRecyclerView: RecyclerView = view.findViewById(R.id.recTasks)

        val mainActivity = activity as MainActivity
        val taskViewModel = mainActivity.taskViewModel
        val taskAdapter = TaskAdapter(taskViewModel)
        taskRecyclerView.adapter = taskAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            taskViewModel.taskFlow.collectLatest {
                taskAdapter.submitData(it)
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as TaskAdapter.TaskViewHolder).task?.let {
                    taskViewModel.deleteById(it)

                }
            }
        }).attachToRecyclerView(taskRecyclerView)
    }
}