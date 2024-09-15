package com.example.byteproject

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

class Datastore(context: Context) :
    SQLiteOpenHelper(context, NAME, null, VERSION) {
    companion object {
        private const val VERSION = 1
        private const val NAME = "LocationDB"
        private const val TABLE_LOCATIONS = "Locations"
        private const val KEY_ID = "id"
        private const val KEY_LAT = "latitude"
        private const val KEY_LON = "longitude"
    }
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_LOCATIONS($KEY_ID INTEGER PRIMARY KEY,$KEY_LAT REAL,$KEY_LON REAL)")
        db.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOCATIONS")
        onCreate(db)
    }
    fun insertloc(lat: Double, lon: Double) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_LAT, lat)
        values.put(KEY_LON, lon)
        db.insert(TABLE_LOCATIONS, null, values)
        db.close()
    }
    fun getAllLoc(): List<Pair<Double, Double>> {
        val locationList: MutableList<Pair<Double, Double>> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_LOCATIONS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LAT))
                val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LON))
                locationList.add(Pair(latitude, longitude))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return locationList
    }
}
