package com.bootcamp.todolist.feature.task

import android.os.Bundle
import android.view.View
import com.bootcamp.todolist.R
import com.bootcamp.todolist.application.TaskApplication
import com.bootcamp.todolist.bases.BaseActivity
import com.bootcamp.todolist.feature.tasklist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.activity_task.toolbar
import java.text.SimpleDateFormat
import java.util.*

class TaskActivity : BaseActivity() {

    private var idtask: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        setupToolBar(toolbar, "Tarefa",true)
        setupTask()
        b_save_task.setOnClickListener { onClickSaveTask() }

        insertDate()

        insertHour()
    }

    private fun setupTask(){
        idtask = intent.getIntExtra("index",-1)
        if (idtask == -1){
            b_delete_task.visibility = View.GONE
            return
        }
        progress.visibility = View.VISIBLE
        Thread(Runnable {
            Thread.sleep(250)
            var list = TaskApplication.instance.helperDB?.search_task("$idtask",true) ?: return@Runnable
            var task = list.getOrNull(0) ?: return@Runnable
            runOnUiThread {
                et_title.setText(task.title)
                et_date.setText(task.date)
                et_hour.setText(task.hour)
                progress.visibility = View.GONE
            }
        }).start()
    }

    private fun onClickSaveTask(){
        val title = et_title.text.toString()
        val date = et_date.text.toString()
        val hour = et_hour.text.toString()
        val task = Task(
                idtask,
                title,
                date,
                hour
        )
        progress.visibility = View.VISIBLE
        Thread(Runnable {
            Thread.sleep(250)
            if(idtask == -1) {
                TaskApplication.instance.helperDB?.saveTask(task)
            }else{
                TaskApplication.instance.helperDB?.updateTask(task)
            }
            runOnUiThread {
                progress.visibility = View.GONE
                finish()
            }
        }).start()
    }

    fun onClickDeleteTask(view: View) {
        if(idtask > -1){
            progress.visibility = View.VISIBLE
            Thread(Runnable {
                //Thread.sleep(1500)
                TaskApplication.instance.helperDB?.deleteTask(idtask)
                runOnUiThread {
                    progress.visibility = View.GONE
                    finish()
                }
            }).start()
        }
    }

    private fun insertDate(){
        et_date.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1

                val date = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(it + offset)

                et_date.setText(date)
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
    }

    private fun insertHour(){
        et_hour.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                et_hour.setText("$hour:$minute")
            }

            timePicker.show(supportFragmentManager, null)
        }
    }
}
