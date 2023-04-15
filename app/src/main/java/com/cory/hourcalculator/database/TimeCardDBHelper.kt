@file:Suppress("CanBeVal", "CanBeVal")

package com.cory.hourcalculator.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TimeCardDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            "CREATE TABLE $TABLE_NAME " +
                    "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT DEFAULT \"\" NOT NULL, $COLUMN_TOTAL TEXT, $COLUMN_WEEK TEXT, $COLUMN_IMAGE TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_IMAGE TEXT")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun insertRow(
        name: String,
        week: String,
        total: String
    ) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
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

    fun addImage(id: String, image: String) {
        val values = ContentValues()
        values.put(COLUMN_IMAGE, image)

        val db = this.writableDatabase

        db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id))
    }

    fun deleteRow(row_id: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(row_id))
        db.close()

    }

    fun getImage(id: String) : Cursor {
        val db = this.writableDatabase

        return db.rawQuery("SELECT $COLUMN_IMAGE FROM $TABLE_NAME WHERE $COLUMN_ID=$id", null)
    }

    fun getAllImages(context: Context) : Cursor {
        val db = this.writableDatabase

        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_IMAGE IS NOT null AND TRIM($COLUMN_IMAGE) != ''", null)
    }

    fun getAllRow(context: Context): Cursor? {
        val db = this.writableDatabase

        return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY week desc", null)
    }

    fun updateName(name: String, id: String) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)

        val db = this.writableDatabase

        db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id))
    }

    fun updateWeek(week: String, id: String) {
        val values = ContentValues()
        values.put(COLUMN_WEEK, week)

        val db = this.writableDatabase

        db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id))
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
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "storedTimeCards.db"
        const val TABLE_NAME = "time_cards"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_TOTAL = "totalHours"
        const val COLUMN_WEEK = "week"
        const val COLUMN_IMAGE = "image"
    }
}