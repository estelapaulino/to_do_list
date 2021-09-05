package com.bootcamp.todolist.application

import android.app.Application
import com.bootcamp.todolist.helpers.HelperDB

class TaskApplication : Application() {

    var helperDB: HelperDB? = null
        private set

    companion object {
        lateinit var instance: TaskApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }
}