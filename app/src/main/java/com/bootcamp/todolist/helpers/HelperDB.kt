package com.bootcamp.todolist.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bootcamp.todolist.feature.tasklist.model.Task

class HelperDB(
    context: Context
) : SQLiteOpenHelper(context, DB, null, CURRENT_VERSION) {

    companion object {
        private val DB = "task.db"
        private val CURRENT_VERSION = 1
    }

    val TABLE_NAME = "task"
    val COLUMN_ID = "id"
    val COLUMN_TITLE = "title"
    val COLUMN_DATE = "date"
    val COLUMN_HOUR = "hour"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ID INTEGER NOT NULL," +
            "$COLUMN_TITLE TEXT NOT NULL," +
            "$COLUMN_DATE TEXT NOT NULL," +
            "$COLUMN_HOUR TEXT NOT NULL," +
            "" +
            "PRIMARY KEY($COLUMN_ID AUTOINCREMENT)" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion) {
            db?.execSQL(DROP_TABLE)
        }
        onCreate(db)
    }

    fun search_task(search: String, isSearchForId: Boolean = false) : List<Task> {
        val db = readableDatabase ?: return mutableListOf()
        var list = mutableListOf<Task>()
        var where: String? = null
        var args: Array<String> = arrayOf()
        if(isSearchForId){
            where = "$COLUMN_ID = ?"
            args = arrayOf("$search")
        }else{
            where = "$COLUMN_TITLE LIKE ?"
            args = arrayOf("%$search%")
        }
        var cursor = db.query(TABLE_NAME,null,where,args,null,null,null)
        if (cursor == null){
            db.close()
            return mutableListOf()
        }
        while(cursor.moveToNext()){
            var task = Task(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HOUR))
            )
            list.add(task)
        }
        db.close()
        return list
    }

    fun saveTask(task: Task) {
        val db = writableDatabase ?: return
        var content = ContentValues()
        content.put(COLUMN_TITLE,task.title)
        content.put(COLUMN_DATE,task.date)
        content.put(COLUMN_HOUR,task.hour)
        db.insert(TABLE_NAME,null,content)
        db.close()
    }

    fun deleteTask(id: Int) {
        val db = writableDatabase ?: return
        val sql = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val arg = arrayOf("$id")
        db.execSQL(sql,arg)
        db.close()
    }

    fun updateTask(task: Task) {
        val db = writableDatabase ?: return
        val sql = "UPDATE $TABLE_NAME SET $COLUMN_TITLE = ?, $COLUMN_DATE = ?, $COLUMN_HOUR = ?  WHERE $COLUMN_ID = ?"
        val arg = arrayOf(task.title, task.date, task.hour, task.id)
        db.execSQL(sql,arg)
        db.close()
    }
}