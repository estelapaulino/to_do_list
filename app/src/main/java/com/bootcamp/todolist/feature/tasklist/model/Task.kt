package com.bootcamp.todolist.feature.tasklist.model

data class Task(
    var id: Int = -1,
    var title: String = "",
    var date: String = "",
    var hour: String = ""
)