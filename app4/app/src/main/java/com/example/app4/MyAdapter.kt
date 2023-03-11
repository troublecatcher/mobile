package com.example.app4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import java.util.Date
import kotlin.math.log

class MyAdapter(private val context: Context, private val arrayList: ArrayList<MyData>, private val arrayList2: ArrayList<String>, private val arrayList3: ArrayList<MyData2>, private val tagIndex: Int) : BaseAdapter() {
    private lateinit var date: TextView
    private lateinit var title: TextView
    private lateinit var text: TextView
    private lateinit var tags: TextView
    override fun getCount(): Int {
        if(tagIndex != -1){
            var size = 0
            for (item in arrayList3)
                if(item.tags.toInt() == tagIndex+1)
                    size++
            return size
        }
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView:View
        convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        date = convertView.findViewById(R.id.liNoteDate)
        title = convertView.findViewById(R.id.liNoteTitle)
        text = convertView.findViewById(R.id.liNoteText)
        tags = convertView.findViewById(R.id.liNoteTags)

        if(tagIndex == -1){
            date.text = arrayList[position].date
            title.text = arrayList[position].title
            text.text = arrayList[position].text
            var string = "Теги: "
            for (i in 0 until arrayList3.count())
                if(position+1 == arrayList3[i].notes.toInt())
                    string += "${arrayList2[arrayList3[i].tags.toInt()-1]}, "
            tags.text = string.dropLast(2)
        }else{
//            if(arrayList3[position].tags.toInt() == tagIndex+1){
//                date.text = arrayList[arrayList3[position].notes.toInt()-1].date
//                title.text = arrayList[arrayList3[position].notes.toInt()-1].title
//                text.text = arrayList[arrayList3[position].notes.toInt()-1].text
//                var string = "Теги: "
//                for (i in 0 until arrayList3.count())
//                    if(arrayList3[position].notes.toInt() == arrayList3[i].notes.toInt())
//                        string += "${arrayList2[arrayList3[i].tags.toInt()-1]}, "
//                tags.text = string.dropLast(2)
//            }
            val resultList = arrayListOf<MyData>()
            if(arrayList3[position].tags.toInt() == tagIndex){
                resultList.add(MyData(arrayList[position].date,
                                      arrayList[position].title,
                                      arrayList[position].text))
            }
            Toast.makeText(context, "$position", Toast.LENGTH_SHORT).show()
        }

        return convertView
    }
}

class MyData(var date: String, var title: String, var text: String)
class MyData2(var notes: String, var tags: String)