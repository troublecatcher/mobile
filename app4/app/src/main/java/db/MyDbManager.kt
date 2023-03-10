package db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.app4.MyData
import db.MyDbNameClass.TABLE_NAME

class MyDbManager (context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = myDbHelper.writableDatabase
    }

    fun insertToDb(date: String, title: String, content: String){
        val values = ContentValues().apply{
            put(MyDbNameClass.COLUMN_NAME_DATE, date)
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }

    fun insertToDb2(tag: String){
        val values = ContentValues().apply{
            put(MyDbNameClass.COLUMN_NAME_TAG, tag)
        }
        db?.insert(MyDbNameClass.TABLE_NAME2, null, values)
    }

    fun readDbData() : ArrayList<MyData> {
        val dataList = ArrayList<MyData>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, null, null, null, null, null)

        while(cursor?.moveToNext()!!){
            val data1 = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_DATE))
            val data2 = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
            val data3 = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CONTENT))
            dataList.add(MyData(data1, data2, data3))
        }
        cursor.close()

        return dataList
    }
    fun updateNote(id: Int, date: String, title: String, content: String){
        val values = ContentValues().apply{
            put(MyDbNameClass.COLUMN_NAME_DATE, date)
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
        }
        db?.update(TABLE_NAME, values,"_id=${id+1}",null)
    }
    fun deleteNote(id: Int, last: Boolean) {
        openDb()
        db?.delete(TABLE_NAME, "_id=${id+1}", null)
        if(!last){
            val strSQL1 = "UPDATE my_table SET _id = (_id +1) WHERE _id < 1"
            val strSQL = "UPDATE my_table SET _id = (_id -1) WHERE _id > 1"
            db!!.execSQL(strSQL)
            db!!.execSQL(strSQL1)
        }
        db?.close()
    }
//    fun updateNote(id: Int) {
//        val cv = ContentValues()
//        cv.put()
//        openDb()
//        db?.update(TABLE_NAME, )
//    }
    fun readDbData2() : ArrayList<String> {
        val dataList = ArrayList<String>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME2, null, null, null, null, null, null)

        while(cursor?.moveToNext()!!){
            val data = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TAG))
            dataList.add(data)
        }
        cursor.close()

        return dataList
    }
    fun closeDb(){
        myDbHelper.close()
    }
}