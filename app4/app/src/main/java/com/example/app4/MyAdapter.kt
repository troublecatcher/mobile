package com.example.app4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MyAdapter(private val context: Context, private val notes: ArrayList<NoteClass>, private val tags: ArrayList<String>, private val noteTagRelations: ArrayList<NoteTagRelationClass>, private val tagIndex: Int) : BaseAdapter() {
    private lateinit var tvDate: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvText: TextView
    private lateinit var tvTags: TextView
    var notesWithThisTag = arrayListOf<Int>()
    override fun getCount(): Int {
        if(tagIndex != -1){
            var size = 0
            for (noteTagRelation in noteTagRelations)
                if(noteTagRelation.tags.toInt() == tagIndex+1){
                    size++
                    notesWithThisTag.add(noteTagRelation.notes.toInt())
                }
            return size
        }
        return notes.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val convertView:View
        convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        tvDate = convertView.findViewById(R.id.liNoteDate)
        tvTitle = convertView.findViewById(R.id.liNoteTitle)
        tvText = convertView.findViewById(R.id.liNoteText)
        tvTags = convertView.findViewById(R.id.liNoteTags)

        if(tagIndex == -1){
            tvDate.text = notes[position].date
            tvTitle.text = notes[position].title
            tvText.text = notes[position].text
            var relatedTags = "Теги: "
            for (i in 0 until noteTagRelations.count())
                if(position+1 == noteTagRelations[i].notes.toInt())
                    relatedTags += "${tags[noteTagRelations[i].tags.toInt()-1]}, "
            tvTags.text = relatedTags.dropLast(2)
        }else{
            val neededNoteIndex = notesWithThisTag[position] - 1
                tvDate.text = notes[neededNoteIndex].date
                tvTitle.text = notes[neededNoteIndex].title
                tvText.text = notes[neededNoteIndex].text
                var relatedTags = "Теги: "
                for (i in 0 until noteTagRelations.count())
                    if(neededNoteIndex + 1 == noteTagRelations[i].notes.toInt())
                        relatedTags += "${tags[noteTagRelations[i].tags.toInt()-1]}, "
                tvTags.text = relatedTags.dropLast(2)
        }
        return convertView
    }
}

class NoteClass(var date: String, var title: String, var text: String)
class NoteTagRelationClass(var notes: String, var tags: String)