package com.example.app4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import com.example.app4.databinding.FragmentNotesBinding
import db.MyDbManager
import java.util.zip.Inflater

class notesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNotesBinding.inflate(inflater)

        binding.goTags.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, tagsFragment.newInstance()).commit()
        }

        binding.newNote.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, newNoteFragment.newInstance(-1, false, "create", "notes")).commit()
        }

        val dbmng = MyDbManager(requireContext())
        dbmng.openDb()
        val dataList = dbmng.readDbData()
        val dataList2 = dbmng.readDbData2()
        val dataList3 = dbmng.readDbData3()
        val notesList:ListView = binding.notesList
        var aa = MyAdapter(requireContext(), dataList, dataList2, dataList3, -1)
        notesList.adapter = aa

        binding.notesList.onItemClickListener = AdapterView.OnItemClickListener { p0, _, p2, _ ->
            val item = p0?.getItemAtPosition(p2)
            val last = p0?.getItemAtPosition(dataList.lastIndex)
            parentFragmentManager.beginTransaction().
            replace(R.id.notesPlaceholder,
                newNoteFragment.newInstance(item.toString().toInt(), item == last, "edit", "notes")).commit()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = notesFragment()
    }
}