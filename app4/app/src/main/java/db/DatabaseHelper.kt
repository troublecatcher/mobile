package db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context:Context):SQLiteOpenHelper(context, DatabaseNames.database_name, null, DatabaseNames.database_version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DatabaseNames.create_table_notes)
        db?.execSQL(DatabaseNames.create_table_tags)
        db?.execSQL(DatabaseNames.create_table_note_tag_relations)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(DatabaseNames.delete_table_notes)
        db?.execSQL(DatabaseNames.delete_table_tags)
        db?.execSQL(DatabaseNames.delete_table_note_tag_relations)
        onCreate(db)
    }
}