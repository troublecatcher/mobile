package db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context:Context):SQLiteOpenHelper(context, MyDbNameClass.DATABASE_NAME, null, MyDbNameClass.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(MyDbNameClass.CREATE_TABLE)
        db?.execSQL(MyDbNameClass.CREATE_TABLE2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(MyDbNameClass.SQL_DELETE_TABLE)
        db?.execSQL(MyDbNameClass.SQL_DELETE_TABLE2)
        onCreate(db)
    }
}