package com.example.planfollower

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planfollower.databinding.FragmentDetailBinding
import com.example.planfollower.databinding.FragmentNotesBinding


class NotesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var noteList : ArrayList<Note>
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val note1 = Note("Shopping List","Book, pencil, apple")
        val note2 = Note("Activity List","Running, yoga, walking")

        noteList=arrayListOf(note1,note2)

        val adapter = NoteAdapter(noteList)
        binding.rvNote.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNote.adapter = adapter
    }


    /*fun goDetailFragment(view: View){
        val action = NotesFragmentDirections.actionNotesFragmentToDetailFragment()
        Navigation.findNavController(view).navigate(action)
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}