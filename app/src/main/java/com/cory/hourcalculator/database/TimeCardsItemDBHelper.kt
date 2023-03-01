@file:Suppress("CanBeVal", "CanBeVal")

package com.cory.hourcalculator.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cory.hourcalculator.classes.SortData

class TimeCardsItemDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            "CREATE TABLE $TABLE_NAME " +
                    "($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_ITEM_ID TEXT, $COLUMN_IN TEXT, $COLUMN_OUT TEXT, $COLUMN_TOTAL TEXT, $COLUMN_DAY NUMERIC, $COLUMN_BREAK TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertRow(
        itemID : String,
        inTime: String,
        outTime: String,
        total: String,
        dayOfWeek: Long,
        breakTime: String
    ) {
        val values = ContentValues()
        values.put(COLUMN_ITEM_ID, itemID)
        values.put(COLUMN_IN, inTime)
        values.put(COLUMN_OUT, outTime)
        values.put(COLUMN_TOTAL, total)
        values.put(COLUMN_DAY, dayOfWeek)
        values.put(COLUMN_BREAK, breakTime)

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

    fun getCountForItemID(id: Int): Int {
        val db = this.readableDatabase
        return DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM $TABLE_NAME WHERE $COLUMN_ITEM_ID=$id", null).toInt()
    }

    fun getAllRow(context: Context, id: String): Cursor? {
        val db = this.writableDatabase
        val sortData = SortData(context)
        val sortType = sortData.loadSortState()

        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ITEM_ID=$id", null)
    }

    fun deleteAllItemRow(context: Context, id: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id))
        db.close()
    }

    fun deleteAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.execSQL("delete from $TABLE_NAME")
        db.close()
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "storedTimeCardItems.db"
        const val TABLE_NAME = "time_card_items"

        const val COLUMN_ID = "id"
        const val COLUMN_ITEM_ID = "itemId"
        const val COLUMN_IN = "inTime"
        const val COLUMN_OUT = "outTime"
        const val COLUMN_TOTAL = "totalHours"
        const val COLUMN_DAY = "date"
        const val COLUMN_BREAK = "breakTime"
    }
}
