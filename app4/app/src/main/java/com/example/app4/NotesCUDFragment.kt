package com.example.app4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.app4.databinding.FragmentCudNotesBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import db.DatabaseManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotesCUDFragment(private val pressedNoteIndex: Int, private val isLastNote: Boolean, private val mode: String, private val whereFrom: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = FragmentCudNotesBinding.inflate(inflater)

        var date: String
        var title: String
        var text: String
        val db = DatabaseManager(requireContext())
        db.openDb()

        binding.saveNote.setOnClickListener {
            date = binding.noteDate.text.toString()
            title = binding.noteName.text.toString()
            text = binding.noteText.text.toString()
            var selectedTags = arrayListOf<Int>()
            val chipGroup: ChipGroup = binding.cgTags
            for (i in 0 until chipGroup.childCount){
                val chip = chipGroup.getChildAt(i) as Chip
                if (chip.isChecked){
                    selectedTags.add(i)
                }
            }
            if(title != "" && text != ""){
                when(mode){
                    "create" -> {
                        db.createNote(date, title, text)
                        Toast.makeText(requireContext(), "Заметка успешно создана", Toast.LENGTH_SHORT).show()
                        if(selectedTags.isNotEmpty()){
                            val notes = db.readNotes()
                            val lastNoteIndex = notes.indexOf(notes.last()) + 1
                            for(tag in selectedTags){
                                db.createNoteTagRelation(lastNoteIndex.toString(), (tag + 1).toString())
                            }
                        }
                    }
                    "edit" -> {
                        db.updateNote(pressedNoteIndex, date, title, text)
                        Toast.makeText(requireContext(), "Заметка успешно изменена", Toast.LENGTH_SHORT).show()
                    }
                }
                
                parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, NotesFragment.newInstance()).commit()
            }else Toast.makeText(requireContext(), "Заполните все необходимые поля", Toast.LENGTH_SHORT).show()
        }
        binding.cancelNote.setOnClickListener {
            when(whereFrom){
                "notes" -> {
                    parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, NotesFragment.newInstance()).commit()
                }
                "tags" -> {
                    parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, TagsFragment.newInstance()).commit()
                }
            }
        }

        binding.delNote.setOnClickListener {
            db.deleteNote(pressedNoteIndex, isLastNote)
            Toast.makeText(requireContext(), "Заметка успешно удалена", Toast.LENGTH_SHORT).show()
            when(whereFrom){
                "notes" -> {
                    parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, NotesFragment.newInstance()).commit()
                }
                "tags" -> {
                    parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, TagsFragment.newInstance()).commit()
                }
            }
        }
        if(pressedNoteIndex != -1){
            val notes = db.readNotes()
            binding.noteDate.setText(notes[pressedNoteIndex].date)
            binding.noteName.setText(notes[pressedNoteIndex].title)
            binding.noteText.setText(notes[pressedNoteIndex].text)
        }
        val tags = db.readTags()
        val noteTagRelations = db.readNoteTagRelations()

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val current = LocalDateTime.now().format(formatter).toString()
        binding.noteDate.setText(current)
        binding.noteDate.isEnabled = false

        when(mode){
            "create" -> {
                val chipGroup: ChipGroup = binding.cgTags
                for (tag in tags){
                    val chip = Chip(requireContext())
                    chip.text = tag
                    chip.isCheckable = true
                    chipGroup.addView(chip)
                }
                binding.delNote.visibility = View.GONE
            }
            "edit" -> {
                val chipGroup: ChipGroup = binding.cgTags
                for(noteTagRelation in noteTagRelations){
                    if(noteTagRelation.notes.toInt() == pressedNoteIndex+1){
                        val chip = Chip(requireContext())
                        chip.text = tags[noteTagRelation.tags.toInt()-1]
                        chip.isChecked = true
                        chipGroup.addView(chip)
                    }
                }
            }
        }
        
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(index: Int, last: Boolean, mode: String, from: String) = NotesCUDFragment(index, last, mode, from)
    }
}