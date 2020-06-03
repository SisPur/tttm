package com.example.orata.utils.auth

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AuthTableHelper (context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ID + "  TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_TOKEN + " TEXT, "
                + COLUMN_LEVEL + " TEXT, "
                + COLUMN_ADDRESS + " TEXT, "
                + COLUMN_PHONE + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_IS_LOGIN + " INT);")
        db.execSQL(CREATE_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun save(){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_ID, "1")
        values.put(COLUMN_NAME, "-")
        values.put(COLUMN_TOKEN, "-")
        values.put(COLUMN_LEVEL, "-")
        values.put(COLUMN_ADDRESS, "-")
        values.put(COLUMN_PHONE, "-")
        values.put(COLUMN_IS_LOGIN, 0)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun update(snapshot: AuthSnapshot) : Int {
        val db = this.writableDatabase
        val updateID: Int
        val where: String = "ID = ?"
        val whereArgs = arrayOf(1.toString())
        val values = ContentValues()

        if (snapshot != null) {
            values.put(COLUMN_ID, snapshot.getId())
            values.put(COLUMN_NAME, snapshot.getName())
            values.put(COLUMN_TOKEN, snapshot.getToken())
            values.put(COLUMN_LEVEL, snapshot.getLevel())
            values.put(COLUMN_ADDRESS, snapshot.getAddress())
            values.put(COLUMN_PHONE, snapshot.getPhone())
            values.put(COLUMN_EMAIL, snapshot.getEmail())
            values.put(COLUMN_IS_LOGIN, snapshot.getIsLogin())
        }

        updateID = db.update(TABLE_NAME, values, where, whereArgs)
        db.close()

        return updateID
    }

    fun getAuth(): AuthSnapshot? {

        val database = this.writableDatabase
        val SQL = ("SELECT * FROM " + TABLE_NAME + " WHERE "
                + ID + " = 1 ")
        val cursor = database.rawQuery(SQL, null)
        if (cursor != null && cursor.moveToFirst()) {
            val user = AuthSnapshot()
            user.setDbID(cursor.getInt(cursor.getColumnIndexOrThrow(ID)))
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)))
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)))
            user.setToken(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOKEN)))
            user.setLevel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)))
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)))
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)))
            user.setIsLogin(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_LOGIN)))
            cursor.close()
            database.close()
            return user
        }
        return null
    }

    fun logout() {
        val db = this.writableDatabase
        val where: String = "ID = ?"
        val whereArgs = arrayOf(1.toString())
        val values = ContentValues()
        values.put(COLUMN_TOKEN, "-")
        values.put(COLUMN_IS_LOGIN, 0)
        db.update(TABLE_NAME, values, where, whereArgs)
        db.close()
    }

    companion object {
        private var mAuthTableHelper: AuthTableHelper? = null

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "tttm.db"

        val TABLE_NAME = "karyawan"
        val ID = "id"
        val COLUMN_ID = "karyawan_id"
        val COLUMN_NAME = "karyawan_nama"
        val COLUMN_TOKEN = "karyawan_token"
        val COLUMN_LEVEL = "karyawan_level"
        val COLUMN_ADDRESS = "karyawan_address"
        val COLUMN_PHONE = "karyawan_phone"
        val COLUMN_EMAIL = "karyawan_email"
        val COLUMN_IS_LOGIN = "karyawan_islogin"
    }
}