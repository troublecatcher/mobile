package com.example.app4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.Date

class MyAdapter(private val context: Context, private val arrayList: ArrayList<MyData>) : BaseAdapter() {
    private lateinit var date: TextView
    private lateinit var title: TextView
    private lateinit var text: TextView
    private lateinit var tags: TextView
    override fun getCount(): Int {
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
        date.text = arrayList[position].date.toString()
        title.text = arrayList[position].title
        text.text = arrayList[position].text
//        tags.text = arrayList[position].tags
        return convertView
    }
}

class MyData(var date: String, var title: String, var text: String)