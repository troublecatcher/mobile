package com.example.app4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.example.app4.databinding.FragmentEditTagsBinding
import db.MyDbManager

class editTagsFragment(private val chip: String, private val mode: String, private val index: Int, private  val last: Boolean) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditTagsBinding.inflate(inflater)

        val dbmng = MyDbManager(requireContext())
        dbmng.openDb()

        binding.cancelTag.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, tagsFragment.newInstance()).commit()
        }

        binding.saveTag.setOnClickListener {
            var tag = ""
            tag = binding.tagName.text.toString()
            if(tag != ""){
                when(mode){
                    "create" -> {
                        dbmng.insertToDb2(tag)
                        Toast.makeText(requireActivity(), "Тег успешно создан", Toast.LENGTH_SHORT).show()
                    }
                    "edit" -> {
                        dbmng.updateTag(index, tag)
                        Toast.makeText(requireContext(), "Тег успешно изменен", Toast.LENGTH_SHORT).show()
                    }
                }
                parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, tagsFragment.newInstance()).commit()
            } else Toast.makeText(requireContext(), "Введите название тега", Toast.LENGTH_SHORT).show()
        }

        val dl = dbmng.readDbData()
        if(mode == "edit"){
            if(chip != ""){
                binding.tagName.setText(chip)
            }
            dbmng.openDb()
            val dataList = dbmng.readDbData()
            val dataList2 = dbmng.readDbData2()
            val dataList3 = dbmng.readDbData3()
            val notesList: ListView = binding.lvNotesWithThisTag
            var aa = MyAdapter(requireContext(), dataList, dataList2, dataList3, index)
            notesList.adapter = aa

            binding.lvNotesWithThisTag.onItemClickListener = AdapterView.OnItemClickListener { p0, _, p2, _ ->
                val tableNoteTag = dbmng.readDbData3()
                val list = arrayListOf<MyData2>()
                for (item in tableNoteTag)
                    if(item.tags.toInt() == index+1)
                        list.add(MyData2(item.notes, item.tags))
                val item = list[p2]
                val last = (dl.lastIndex + 1).toString()
                parentFragmentManager.beginTransaction().
                replace(R.id.notesPlaceholder,
                    newNoteFragment.newInstance(item.notes.toInt()-1, item.notes == last, "edit", "tags")).commit()
            }
        }else binding.twNotesWithThisTag.text = ""

        binding.delTag.setOnClickListener {
            dbmng.deleteTag(index, last)
            Toast.makeText(requireContext(), "Тег успешно удален", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, tagsFragment.newInstance()).commit()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(chip: String, mode: String, index: Int, last: Boolean) = editTagsFragment(chip, mode, index, last)
    }
}