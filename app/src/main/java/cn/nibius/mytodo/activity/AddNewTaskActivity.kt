package cn.nibius.mytodo.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import cn.nibius.mytodo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class AddNewTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)

        val intent: Intent = getIntent()
        val callAction = intent.getStringExtra("callAction")

        val editTitle = findViewById<TextView>(R.id.editTaskTitle)
        val editDetail = findViewById<TextView>(R.id.editTaskDetail)

        if (callAction == "modify") {
            editTitle.text = intent.getStringExtra("taskTitle")
            editDetail.text = intent.getStringExtra("taskDetail")
            title = "Modify Task"
        }

        val btnSaveTask = findViewById<FloatingActionButton>(R.id.fabSaveTask)
        btnSaveTask.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitle.text)) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_empty_title),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val title = editTitle.text.toString()
                val detail = editDetail.text.toString()
                val timeInMillis = Calendar.getInstance().timeInMillis
                replyIntent.putExtra(REPLY_TITLE, title)
                replyIntent.putExtra(REPLY_DETAIL, detail)
                replyIntent.putExtra("responseAction", callAction)
                when (callAction) {
                    "new" -> replyIntent.putExtra("taskId", timeInMillis)
                    "modify" -> {
                        val taskId = intent.getLongExtra("taskId", 50L)
                        replyIntent.putExtra("taskId", taskId)
                    }
                }
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    companion object {
        const val REPLY_TITLE = "cn.nibius.mytodo.tasklistsql.REPLY"
        const val REPLY_DETAIL = "cn.nibius.mytodo.tasklistsql.DETAIL"
    }
}