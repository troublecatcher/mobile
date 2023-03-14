package com.example.app4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app4.databinding.FragmentTagsBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import db.DatabaseManager

class TagsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTagsBinding.inflate(inflater)

        binding.switchToNotes.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, NotesFragment.newInstance()).commit()
        }
        binding.newTag.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentsPlaceholder, TagsCUDFragment.newInstance("", "create", -1,false)).commit()
        }

        val db = DatabaseManager(requireContext())
        db.openDb()
        val tags = db.readTags()
        val chipGroup: ChipGroup = binding.cgTags
        for (tag in tags){
            val chip = Chip(requireContext())
            chip.text = tag
            chip.setOnClickListener {
                val chipName = chip.text.toString()
                parentFragmentManager.beginTransaction().
                replace(R.id.fragmentsPlaceholder,
                    TagsCUDFragment.newInstance
                        (chipName, "edit", chipGroup.indexOfChild(chip), chipGroup.indexOfChild(chip) == chipGroup.indexOfChild(chipGroup.getChildAt(chipGroup.childCount-1))))
                    .commit()
            }
            chipGroup.addView(chip)
        }
        
        return binding.root
    }

    companion object {
        fun newInstance() = TagsFragment()
    }
}