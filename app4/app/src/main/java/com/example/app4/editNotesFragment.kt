package com.example.app4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.iterator
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
            var tags = arrayListOf<Int>()
            val chipGroup: ChipGroup = binding.cgTags
            for (i in 0 until chipGroup.childCount){
                val chip = chipGroup.getChildAt(i) as Chip
                if (chip.isChecked){
                    tags.add(i)
                }
            }
            if(date != "" && title != "" && text != ""){
//                Toast.makeText(requireContext(), "$tags", Toast.LENGTH_SHORT).show()
                dbmng.openDb()
                when(mode){
                    "create" -> {
                        dbmng.insertToDb(date, title, text)
                        Toast.makeText(requireContext(), "Заметка успешно создана", Toast.LENGTH_SHORT).show()
                        if(tags.isNotEmpty()){
                            val dataList = dbmng.readDbData()
                            val idx = dataList.indexOf(dataList.last()) + 1
                            for(tag in tags){
                                dbmng.insertToDb3(idx.toString(), (tag + 1).toString())
                            }
                        }
                    }
                    "edit" -> {
                        dbmng.updateNote(index, date, title, text)
                        Toast.makeText(requireContext(), "Заметка успешно изменена", Toast.LENGTH_SHORT).show()
                    }
                }
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

        dbmng.openDb()
        if(index != -1){
            val dataList = dbmng.readDbData()
            binding.noteDate.setText(dataList[index].date)
            binding.noteName.setText(dataList[index].title)
            binding.noteText.setText(dataList[index].text)
        }
        val dataList2 = dbmng.readDbData2()
        val dataList3 = dbmng.readDbData3()
        when(mode){
            "create" -> {
                val chipGroup: ChipGroup = binding.cgTags
                for (item in dataList2){
                    val chip = Chip(requireContext())
                    chip.text = item
                    chip.isCheckable = true
                    chipGroup.addView(chip)
                }
            }
            "edit" -> {
                val chipGroup: ChipGroup = binding.cgTags
                for(item in dataList3){
                    if(item.notes.toInt() == index+1){
                        val chip = Chip(requireContext())
                        chip.text = dataList2[item.tags.toInt()-1]
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
        fun newInstance(index: Int, last: Boolean, mode: String) = newNoteFragment(index, last, mode)
    }
}