package com.bootcamp.todolist.feature.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bootcamp.todolist.R
import com.bootcamp.todolist.application.TaskApplication
import com.bootcamp.todolist.bases.BaseActivity
import com.bootcamp.todolist.feature.task.TaskActivity
import com.bootcamp.todolist.feature.tasklist.adapter.TaskAdapter
import com.bootcamp.todolist.feature.tasklist.model.Task
import kotlinx.android.synthetic.main.activity_main.*

import java.lang.Exception



class MainActivity : BaseActivity() {

    private var adapter:TaskAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolBar(toolbar, "Tarefas",false)
        setupListView()
        setupOnClicks()
    }

    private fun setupOnClicks(){
        fab.setOnClickListener { onClickAdd() }
        iv_search.setOnClickListener { onClickSearch() }
    }

    private fun setupListView(){
        recycler_view.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        onClickSearch()
    }

    private fun onClickAdd(){
        val intent = Intent(this,TaskActivity::class.java)
        startActivity(intent)
    }

    private fun onClickItemRecyclerView(index: Int){
        val intent = Intent(this,TaskActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

    private fun onClickSearch(){
        val search = et_search.text.toString()
        progress.visibility = View.VISIBLE

        Thread(Runnable {
            Thread.sleep(250)
            var list: List<Task> = mutableListOf()

            try {
                list = TaskApplication.instance.helperDB?.search_task(search) ?: mutableListOf()
            }catch (ex: Exception){
                ex.printStackTrace()
            }
            runOnUiThread {
                adapter = TaskAdapter(this,list) {onClickItemRecyclerView(it)}
                recycler_view.adapter = adapter
                progress.visibility = View.GONE
                if (list.isEmpty()){
                    include_empty.visibility = View.VISIBLE
                    et_search.visibility = View.GONE
                    iv_search.visibility = View.GONE
                }
                else{
                    include_empty.visibility = View.GONE
                    et_search.visibility = View.VISIBLE
                    iv_search.visibility = View.VISIBLE
                }
            }
        }).start()
    }

}
