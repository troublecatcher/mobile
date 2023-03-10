package com.example.app4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
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
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, editTagsFragment.newInstance()).commit()
        }

        val dbmng = MyDbManager(requireContext())
        dbmng.openDb()
        val dataList = dbmng.readDbData2()
        val chipGroup: ChipGroup = binding.cgTags
        for (item in dataList){
            val chip = Chip(requireContext())
            chip.text = item
            chipGroup.addView(chip)
        }
        dbmng.closeDb()

        return binding.root
    }

    companion object {
        fun newInstance() = tagsFragment()

    }
}