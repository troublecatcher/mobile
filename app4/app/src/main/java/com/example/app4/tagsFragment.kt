package com.example.app4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.example.app4.databinding.FragmentTagsBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import db.MyDbManager

class tagsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTagsBinding.inflate(inflater)

        binding.goNotes.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, notesFragment.newInstance()).commit()
        }
        binding.newTag.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, editTagsFragment.newInstance("", "create", -1,false)).commit()
        }

        val dbmng = MyDbManager(requireContext())
        dbmng.openDb()
        val dataList = dbmng.readDbData2()
        val chipGroup: ChipGroup = binding.cgTags
        for (item in dataList){
            val chip = Chip(requireContext())
            chip.text = item
//            chip.setOnClickListener() = AdapterView.OnItemClickListener { p0, _, p2, _ ->
//                val item = p0?.getItemAtPosition(p2)
//                val last = p0?.getItemAtPosition(dataList.lastIndex)
//                parentFragmentManager.beginTransaction().
//                replace(R.id.notesPlaceholder,
//                    newNoteFragment.newInstance(item.toString().toInt(), item == last, "edit")).commit()
//            }
            chip.setOnClickListener {
                val chipName = chip.text.toString()
                parentFragmentManager.beginTransaction().
                replace(R.id.notesPlaceholder,
                    editTagsFragment.newInstance
                        (chipName, "edit", chipGroup.indexOfChild(chip), chipGroup.indexOfChild(chip) == chipGroup.indexOfChild(chipGroup.getChildAt(chipGroup.childCount-1))))
                    .commit()
            }
            chipGroup.addView(chip)
        }
        return binding.root
    }

    companion object {
        fun newInstance() = tagsFragment()

    }
}