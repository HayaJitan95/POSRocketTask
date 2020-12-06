package com.posrocket.haya

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.posrocket.haya.Model.Customer


class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val TABLE_CUSTOMERS: String = "customers_table"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $TABLE_CUSTOMERS (DATA TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMERS")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Customer.db"
    }

    fun insertData(data: List<Customer>?, TABLE_NAME: String?) {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        val gson = Gson()
        val inputString = gson.toJson(data)

        contentValues.put("DATA", inputString)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    fun getAllData(TABLE_NAME: String): Cursor? {
        val db = this.writableDatabase
        return db.rawQuery("select * from $TABLE_NAME", null)
    }


    fun deleteData(TABLE_NAME: String) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
    }

}