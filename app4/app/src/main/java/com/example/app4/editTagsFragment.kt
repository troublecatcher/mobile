package com.example.app4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.app4.databinding.FragmentEditTagsBinding
import com.example.app4.databinding.FragmentTagsBinding
import db.MyDbManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [editTagsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class editTagsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditTagsBinding.inflate(inflater)

        binding.cancelTag.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, tagsFragment.newInstance()).commit()
        }

        binding.saveTag.setOnClickListener {
            val dbmng = MyDbManager(requireContext())

            var tag = ""
            tag = binding.tagName.text.toString()

            if(tag != ""){
                dbmng.openDb()
                dbmng.insertToDb2(tag)
            }
            Toast.makeText(requireActivity(), "Тег '$tag' успешно создан", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction().replace(R.id.notesPlaceholder, tagsFragment.newInstance()).commit()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = editTagsFragment()
    }
}