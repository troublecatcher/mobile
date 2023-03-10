package db

import android.provider.BaseColumns

object MyDbNameClass:BaseColumns {
    const val TABLE_NAME = "my_table"
    const val COLUMN_NAME_DATE = "date"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_CONTENT = "subtitle"

    const val TABLE_NAME2 = "my_table2"
    const val COLUMN_NAME_TAG = "tag"

    const val DATABASE_VERSION = 12
    const val DATABASE_NAME = "NotesDb.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$COLUMN_NAME_DATE TEXT," +
            "$COLUMN_NAME_TITLE TEXT," +
            "$COLUMN_NAME_CONTENT TEXT)"
    const val CREATE_TABLE2 = "CREATE TABLE IF NOT EXISTS $TABLE_NAME2 (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$COLUMN_NAME_TAG TEXT)"

    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    const val SQL_DELETE_TABLE2 = "DROP TABLE IF EXISTS $TABLE_NAME2"

}