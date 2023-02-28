@file:Suppress("CanBeVal", "CanBeVal")

package com.cory.hourcalculator.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cory.hourcalculator.classes.SortData

class TimeCardDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            "CREATE TABLE $TABLE_NAME " +
                    "($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_TOTAL TEXT, $COLUMN_WEEK TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertRow(
        week: String,
        total: String
    ) {
        val values = ContentValues()
        values.put(COLUMN_WEEK, week)
        values.put(COLUMN_TOTAL, total)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getCount(): Int {
        val db = this.readableDatabase
        return DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM $TABLE_NAME", null).toInt()

    }

    fun deleteRow(row_id: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(row_id))
        db.close()

    }

    fun getRow(row_id: String): Cursor {

        val db = this.writableDatabase

        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID=$row_id", null)

    }

    fun getAllRow(context: Context): Cursor? {
        val db = this.writableDatabase
        val sortData = SortData(context)
        val sortType = sortData.loadSortState()

        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun getLastRow(context: Context): Cursor? {
        val db = this.writableDatabase

        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun getLatestRowID() : Cursor? {
        val db = this.writableDatabase

        return db.rawQuery("SELECT $COLUMN_ID from $TABLE_NAME order by $COLUMN_ID DESC limit 1", null)
    }

    fun deleteAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.execSQL("delete from $TABLE_NAME")
        db.close()
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "storedTimeCards.db"
        const val TABLE_NAME = "time_cards"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_TOTAL = "totalHours"
        const val COLUMN_WEEK = "week"
    }
}
