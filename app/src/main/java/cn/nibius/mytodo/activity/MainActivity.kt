package cn.nibius.mytodo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.nibius.mytodo.room.Task
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import cn.nibius.mytodo.*
import cn.nibius.mytodo.fragment.TaskDetailFragment
import cn.nibius.mytodo.fragment.TaskListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val TAG = "Main activity"

    private val newTaskActivityRequestCode = 1
    val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TasksApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<TaskListFragment>(R.id.mainFragmentContainerView)
            }
        }

        val btnAddTask: FloatingActionButton = findViewById(R.id.fabAddTask)
        btnAddTask.setOnClickListener { fabOnClick() }
    }

    private fun fabOnClick() {
//        val intent = Intent(this, AddNewTaskActivity::class.java)
//        intent.putExtra("callAction", "new")
//        startActivityForResult(intent, newTaskActivityRequestCode)

        supportFragmentManager.commit {
            replace<TaskDetailFragment>(R.id.mainFragmentContainerView)
            setReorderingAllowed(true)
            addToBackStack("newTask")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newTaskActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val taskTitle =
                intentData?.getStringExtra(AddNewTaskActivity.REPLY_TITLE) ?: "empty title"
            val taskDetail = intentData?.getStringExtra(AddNewTaskActivity.REPLY_DETAIL)
            val taskId = intentData?.getLongExtra("taskId", 0L) ?: 0L
            val task = Task(
                taskId,
                taskTitle,
                false,
                taskDetail,
                taskId
            )
            val responseAction = intentData?.getStringExtra("responseAction") ?: "modify"
            Toast.makeText(this, responseAction, Toast.LENGTH_LONG).show()
            when (responseAction) {
                "new" -> taskViewModel.insert(task)
                "modify" -> taskViewModel.modify(task)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}