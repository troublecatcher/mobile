package com.example.app4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.app4.databinding.FragmentEditNotesBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import db.MyDbManager

class newNoteFragment(private val index: Int, private val last: Boolean, private val mode: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = FragmentEditNotesBinding.inflate(inflater)

        var date = ""
        var title = ""
        var text = ""
        val dbmng = MyDbManager(requireContext())
        binding.saveNote.setOnClickListener {
            date = binding.noteDate.text.toString()
            title = binding.noteName.text.toString()
            text = binding.noteText.text.toString()
            if(date != "" && title != "" && text != ""){
                dbmng.openDb()
                when(mode){
                    "create" -> {
                        dbmng.insertToDb(date, title, text)
                        Toast.makeText(requireContext(), "Заметка успешно создана", Toast.LENGTH_SHORT).show()
                    }
                    "edit" -> {
                        dbmng.updateNote(index, date, title, text)
                        Toast.makeText(requireContext(), "Заметка успешно изменена", Toast.LENGTH_SHORT).show()
                    }
                }
                dbmng.closeDb()
                parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, notesFragment.newInstance()).commit()

            }
        }
        binding.cancelNote.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, notesFragment.newInstance()).commit()
        }

        binding.delNote.setOnClickListener {
            dbmng.deleteNote(index, last)
            Toast.makeText(requireContext(), "Заметка успешно удалена", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, notesFragment.newInstance()).commit()
        }

        if(index != -1){
            dbmng.openDb()
            val dataList = dbmng.readDbData()
            binding.noteDate.setText(dataList[index].date)
            binding.noteName.setText(dataList[index].title)
            binding.noteText.setText(dataList[index].text)

            val dataList2 = dbmng.readDbData2()
            val chipGroup: ChipGroup = binding.cgTags
            for (item in dataList2){
                val chip = Chip(requireContext())
                chip.text = item
                chipGroup.addView(chip)
            }

            dbmng.closeDb()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(index: Int, last: Boolean, mode: String) = newNoteFragment(index, last, mode)
    }
}