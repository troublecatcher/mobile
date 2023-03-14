package com.example.app4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.example.app4.databinding.FragmentCudTagsBinding
import db.DatabaseManager

class TagsCUDFragment(private val chipName: String, private val mode: String, private val tagIndex: Int, private  val isLastTag: Boolean) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCudTagsBinding.inflate(inflater)

        binding.cancelTag.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, TagsFragment.newInstance()).commit()
        }

        val db = DatabaseManager(requireContext())
        db.openDb()

        binding.saveTag.setOnClickListener {
            val tagTitle: String = binding.tagName.text.toString()
            if(tagTitle != ""){
                when(mode){
                    "create" -> {
                        db.createTag(tagTitle)
                        Toast.makeText(requireActivity(), "Тег успешно создан", Toast.LENGTH_SHORT).show()
                    }
                    "edit" -> {
                        db.updateTag(tagIndex, tagTitle)
                        Toast.makeText(requireContext(), "Тег успешно изменен", Toast.LENGTH_SHORT).show()
                    }
                }
                parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, TagsFragment.newInstance()).commit()
            } else Toast.makeText(requireContext(), "Введите название тега", Toast.LENGTH_SHORT).show()
        }

        if(mode == "edit"){
            if(chipName != ""){
                binding.tagName.setText(chipName)
            }
            db.openDb()
            val notes = db.readNotes()
            val tags = db.readTags()
            val noteTagRelations = db.readNoteTagRelations()
            val notesListView: ListView = binding.lvNotesWithThisTag
            notesListView.adapter = MyAdapter(requireContext(), notes, tags, noteTagRelations, tagIndex)

            binding.lvNotesWithThisTag.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val notesWithThisTag = arrayListOf<NoteTagRelationClass>()
                for (noteTagRelation in noteTagRelations)
                    if(noteTagRelation.tags.toInt() == tagIndex+1)
                        notesWithThisTag.add(NoteTagRelationClass(noteTagRelation.notes, noteTagRelation.tags))
                val neededNote = notesWithThisTag[position]
                val lastNoteIndex = (notes.lastIndex + 1).toString()
                parentFragmentManager.beginTransaction().
                replace(R.id.fragmentsPlaceholder,
                    NotesCUDFragment.newInstance(neededNote.notes.toInt()-1, neededNote.notes == lastNoteIndex, "edit", "tags")).commit()
            }
        }else{
            binding.tvNotesWithThisTag.text = ""
            binding.delTag.visibility = View.GONE
        }

        binding.delTag.setOnClickListener {
            db.deleteTag(tagIndex, isLastTag)
            Toast.makeText(requireContext(), "Тег успешно удален", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, TagsFragment.newInstance()).commit()
        }
        
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(chip: String, mode: String, index: Int, last: Boolean) = TagsCUDFragment(chip, mode, index, last)
    }
}