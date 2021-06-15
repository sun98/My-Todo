@file:Suppress("PrivatePropertyName")

package cn.nibius.mytodo.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

    val taskViewModel by viewModels<TaskViewModel> {
        TaskViewModelFactory(application)
    }
    var taskMax = 0L


    private var btnAddTask: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<TaskListFragment>(R.id.mainFragmentContainerView)
            }
        }

        taskViewModel.taskMaxInd.observe(this, {
            taskMax = it
        })

        btnAddTask = findViewById(R.id.fabAddTask)
        setFabOnClickListener()
    }

    private fun fabOnClick() {
        Log.d(TAG, "fabOnClick: ")
        val bundle = Bundle()
        bundle.putString("action", "newTask")

        supportFragmentManager.commit {
            this.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            replace<TaskDetailFragment>(R.id.mainFragmentContainerView, args = bundle)
            setReorderingAllowed(true)
            addToBackStack("newTask")
        }
    }


    fun getFab(): FloatingActionButton {
        return btnAddTask!!
    }

    fun setFabOnClickListener() {
        btnAddTask?.setOnClickListener { fabOnClick() }
    }
}
