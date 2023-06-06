package com.example.carquiz

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION){
    companion object{
        private const val DATABASE_VERSION=1
        private const val DATABASE_NAME="EmployeeDatabase"
        private const val TABLE_CONTACTS="EmployeeTable"
        private const val KEY_ID="_id"
        private const val KEY_NAME="name"
        private const val Key_SCORE="score"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = "CREATE TABLE $TABLE_CONTACTS ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $Key_SCORE TEXT)"
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(/* sql = */ "DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addEmployee(emp:EmpModelClass):Long{
        val db=this.writableDatabase
        val contentValues=ContentValues()
        contentValues.put(KEY_NAME,emp.name)
        contentValues.put(Key_SCORE,emp.score)
        val success=db.insert(TABLE_CONTACTS,null,contentValues)
        db.close()
        return success
    }
    fun updateEmployee(emp:EmpModelClass):Int{
        val db=this.writableDatabase
        val contentValues=ContentValues()
        contentValues.put(KEY_NAME,emp.name)
        contentValues.put(Key_SCORE,emp.score)
        val success=db.update(TABLE_CONTACTS,contentValues,KEY_ID+"="+emp.id,null)
        db.close()
        return success
    }
    fun deleteEmployee(emp:EmpModelClass):Int{
        val db=this.writableDatabase
        val contentValues=ContentValues()
        contentValues.put(KEY_ID,emp.id)
        val success=db.delete(TABLE_CONTACTS,KEY_ID+"="+emp.id,null)
        db.close()
        return success
    }
    fun viewEmployee():ArrayList<EmpModelClass>{
        val empList:ArrayList<EmpModelClass> = ArrayList()
        val selectQuery="SELECT *FROM $TABLE_CONTACTS"
        val db=this.readableDatabase
        var cursor: Cursor?
        try{
            cursor=db.rawQuery(selectQuery,null)
        }catch (e:SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id:Int
        var name:String
        var score:String
        if(cursor.moveToFirst())
        {
            do{
                id=cursor.getInt(if(cursor.getColumnIndex(KEY_ID)!=-1){cursor.getColumnIndex(KEY_ID)}
                else
                {
                    0
                })
                name= cursor.getString(if(cursor.getColumnIndex(KEY_NAME)!=-1){cursor.getColumnIndex(KEY_NAME)}
                else
                {
                    0
                })
                score= cursor.getString(if(cursor.getColumnIndex(Key_SCORE)!=-1){cursor.getColumnIndex(
                    Key_SCORE)}
                else
                {
                    0
                })
                val emp=EmpModelClass(id=id,name=name,score=score)
                empList.add(emp)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return empList

    }
}