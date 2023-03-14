package db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.app4.NoteClass
import com.example.app4.NoteTagRelationClass
import db.DatabaseNames.table_notes
import db.DatabaseNames.table_tags
import db.DatabaseNames.table_note_tag_relations

class DatabaseManager (context: Context) {
    val databaseHelper = DatabaseHelper(context)
    var db: SQLiteDatabase? = null
    fun openDb() {
        db = databaseHelper.writableDatabase
    }
    fun createNote(date: String, title: String, content: String){
        val values = ContentValues().apply{
            put(DatabaseNames.column_notes_date, date)
            put(DatabaseNames.column_notes_title, title)
            put(DatabaseNames.column_notes_text, content)
        }
        db?.insert(table_notes, null, values)
    }
    fun createTag(tag: String){
        val values = ContentValues().apply{
            put(DatabaseNames.column_tags_title, tag)
        }
        db?.insert(table_tags, null, values)
    }
    fun createNoteTagRelation(note: String, tag: String){
        val values = ContentValues().apply{
            put(DatabaseNames.column_note_tag_relations_note, note)
            put(DatabaseNames.column_note_tag_relations_tag, tag)
        }
        db?.insert(table_note_tag_relations, null, values)
    }
    @SuppressLint("Range")
    fun readNotes() : ArrayList<NoteClass> {
        val dataList = ArrayList<NoteClass>()
        val cursor = db?.query(table_notes, null, null, null, null, null, null)

        while(cursor?.moveToNext()!!){
            val data1 = cursor.getString(cursor.getColumnIndex(DatabaseNames.column_notes_date))
            val data2 = cursor.getString(cursor.getColumnIndex(DatabaseNames.column_notes_title))
            val data3 = cursor.getString(cursor.getColumnIndex(DatabaseNames.column_notes_text))
            dataList.add(NoteClass(data1, data2, data3))
        }
        cursor.close()

        return dataList
    }
    @SuppressLint("Range")
    fun readTags() : ArrayList<String> {
        val dataList = ArrayList<String>()
        val cursor = db?.query(table_tags, null, null, null, null, null, null)

        while(cursor?.moveToNext()!!){
            val data = cursor.getString(cursor.getColumnIndex(DatabaseNames.column_tags_title))
            dataList.add(data)
        }
        cursor.close()

        return dataList
    }
    @SuppressLint("Range")
    fun readNoteTagRelations() : ArrayList<NoteTagRelationClass> {
        val dataList = ArrayList<NoteTagRelationClass>()
        val cursor = db?.query(table_note_tag_relations, null, null, null, null, null, null)

        while(cursor?.moveToNext()!!){
            val data1 = cursor.getString(cursor.getColumnIndex(DatabaseNames.column_note_tag_relations_note))
            val data2 = cursor.getString(cursor.getColumnIndex(DatabaseNames.column_note_tag_relations_tag))
            dataList.add(NoteTagRelationClass(data1, data2))
        }
        cursor.close()

        return dataList
    }
    fun updateNote(id: Int, date: String, title: String, content: String){
        val values = ContentValues().apply{
            put(DatabaseNames.column_notes_date, date)
            put(DatabaseNames.column_notes_title, title)
            put(DatabaseNames.column_notes_text, content)
        }
        db?.update(table_notes, values,"_id=${id+1}",null)
    }
    fun updateTag(id: Int, tag: String){
        val values = ContentValues().apply{
            put(DatabaseNames.column_tags_title, tag)
        }
        db?.update(table_tags, values,"_id=$id+1",null)
    }
    fun deleteNote(id: Int, last: Boolean) {
        openDb()
        db?.delete(table_notes, "_id=${id+1}", null)
        db?.delete(table_note_tag_relations, "note=${id+1}", null)
        if(!last){
            val strSQL1 = "UPDATE notes SET _id = (_id +1) WHERE _id < 1"
            val strSQL2 = "UPDATE notes SET _id = (_id -1) WHERE _id > 1"
            db!!.execSQL(strSQL1)
            db!!.execSQL(strSQL2)
            val strSQL3 = "UPDATE note_tag_relations SET note = (note +1) WHERE note < 1"
            val strSQL4 = "UPDATE note_tag_relations SET note = (note -1) WHERE note > 1"
            db!!.execSQL(strSQL3)
            db!!.execSQL(strSQL4)
        }
    }
    fun deleteTag(id: Int, last: Boolean){
        openDb()
        db?.delete(table_tags, "_id=${id+1}", null)
        db?.delete(table_note_tag_relations, "tag=${id+1}", null)
        if(!last){
            val strSQL1 = "UPDATE tags SET _id = (_id +1) WHERE _id < 1"
            val strSQL2 = "UPDATE tags SET _id = (_id -1) WHERE _id > 1"
            db!!.execSQL(strSQL1)
            db!!.execSQL(strSQL2)
            val strSQL3 = "UPDATE note_tag_relations SET tag = (tag +1) WHERE tag < 1"
            val strSQL4 = "UPDATE note_tag_relations SET tag = (tag -1) WHERE tag > 1"
            db!!.execSQL(strSQL3)
            db!!.execSQL(strSQL4)
        }
    }
    fun closeDb(){
        databaseHelper.close()
    }
}