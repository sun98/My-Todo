@file:Suppress("LocalVariableName")

package cn.nibius.mytodo.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cn.nibius.mytodo.activity.MainActivity
import cn.nibius.mytodo.R
import cn.nibius.mytodo.TaskAdapter
import cn.nibius.mytodo.room.Task
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskListFragment : Fragment(R.layout.fragment_task_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val TAG = "task list fragment"
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
            private var fromTask: Task? = null

            private var fromInd: Long? = null
            private var toInd: Long? = null
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
//                Log.d(TAG, "onMove: ${viewHolder.layoutPosition}, ${target.layoutPosition}")
                if (fromTask == null) fromTask = (viewHolder as TaskAdapter.TaskViewHolder).task!!
                if (fromInd == null) fromInd =
                    (viewHolder as TaskAdapter.TaskViewHolder).task!!.taskInd
                toInd = (target as TaskAdapter.TaskViewHolder).task!!.taskInd
                taskAdapter.notifyItemMoved(viewHolder.layoutPosition, target.layoutPosition)
                return true
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                Log.d(TAG, "clearView: ${fromInd},${toInd}")
                if (fromInd != null && toInd != null) {
                    if (fromInd!! < toInd!!) {
                        taskViewModel.move(fromInd!! + 1, toInd!!, -1)
                        Log.d(TAG, "clearView: ${fromInd!! + 1} ~ ${toInd!!} -> -1")
                    } else if (fromInd!! > toInd!!) {
                        taskViewModel.move(toInd!!, fromInd!! - 1, 1)
                        Log.d(TAG, "clearView: ${toInd!!} ~ ${fromInd!! - 1} -> +1")
                    }
                    taskViewModel.reOrder(fromTask!!.taskId, toInd!!)
                }
                fromInd = null
                toInd = null
                fromTask = null
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                Log.d(TAG, "onSwiped: $viewHolder, $direction")
                if ((direction == ItemTouchHelper.LEFT) or (direction == ItemTouchHelper.RIGHT)) {
                    val task = (viewHolder as TaskAdapter.TaskViewHolder).task
//                    Log.d(TAG, "onSwiped: $viewHolder, $direction, ${task?.taskId}")
                    task?.let {
                        taskViewModel.delete(it)
                    }
                }
            }
        }).attachToRecyclerView(taskRecyclerView)
    }
}