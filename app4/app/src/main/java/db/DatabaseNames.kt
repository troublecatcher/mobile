package db

import android.provider.BaseColumns

object DatabaseNames:BaseColumns {
    const val table_notes = "notes"
    const val column_notes_date = "date"
    const val column_notes_title = "title"
    const val column_notes_text = "text"

    const val table_tags = "tags"
    const val column_tags_title = "title"

    const val table_note_tag_relations = "note_tag_relations"
    const val column_note_tag_relations_note = "note"
    const val column_note_tag_relations_tag = "tag"


    const val database_version = 2
    const val database_name = "app4.db"

    const val create_table_notes = "CREATE TABLE IF NOT EXISTS $table_notes (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$column_notes_date TEXT," +
            "$column_notes_title TEXT," +
            "$column_notes_text TEXT)"
    const val create_table_tags = "CREATE TABLE IF NOT EXISTS $table_tags (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$column_tags_title TEXT)"
    const val create_table_note_tag_relations = "CREATE TABLE IF NOT EXISTS $table_note_tag_relations (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$column_note_tag_relations_note TEXT," +
            "$column_note_tag_relations_tag TEXT)"

    const val delete_table_notes = "DROP TABLE IF EXISTS $table_notes"
    const val delete_table_tags = "DROP TABLE IF EXISTS $table_tags"
    const val delete_table_note_tag_relations = "DROP TABLE IF EXISTS $table_note_tag_relations"
}