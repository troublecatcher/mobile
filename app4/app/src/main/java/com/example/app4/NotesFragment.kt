package com.example.app4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.example.app4.databinding.FragmentNotesBinding
import db.DatabaseManager

class NotesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNotesBinding.inflate(inflater)

        binding.switchToTags.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, TagsFragment.newInstance()).commit()
        }

        binding.createNote.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, NotesCUDFragment.newInstance(-1, false, "create", "notes")).commit()
        }

        val db = DatabaseManager(requireContext())
        db.openDb()
        val notes = db.readNotes()
        val tags = db.readTags()
        val noteTagRelations = db.readNoteTagRelations()
        val notesListView:ListView = binding.lvNotes
        notesListView.adapter = MyAdapter(requireContext(), notes, tags, noteTagRelations, -1)

        binding.lvNotes.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val pressedNote = parent?.getItemAtPosition(position)
            val lastNote = parent?.getItemAtPosition(notes.lastIndex)
            parentFragmentManager.beginTransaction().
            replace(R.id.fragmentsPlaceholder,
                NotesCUDFragment.newInstance(pressedNote.toString().toInt(), pressedNote == lastNote, "edit", "notes")).commit()
        }
        
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = NotesFragment()
    }
}