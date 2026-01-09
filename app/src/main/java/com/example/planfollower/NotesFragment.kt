package com.example.planfollower

import android.R.id.list
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planfollower.databinding.FragmentNotesBinding


class NotesFragment : Fragment() , PopupMenu.OnMenuItemClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var noteList : ArrayList<Note>
    private lateinit var popup : PopupMenu
    private val viewModel: NotesViewModel by activityViewModels()
    private lateinit var adapter : NoteAdapter
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

        adapter = NoteAdapter(arrayListOf())
        binding.rvNote.adapter = adapter
        binding.rvNote.layoutManager = LinearLayoutManager(requireContext())


        viewModel.noteList.observe(viewLifecycleOwner) {
            adapter.noteList.clear()
            adapter.noteList.addAll(it)
            adapter.notifyDataSetChanged()
        }


        popup = PopupMenu(requireContext(),binding.floatingActionButton)

        val inflater = popup.menuInflater
        inflater.inflate(R.menu.popup_menu,popup.menu)
        popup.setOnMenuItemClickListener(this)

        binding.floatingActionButton.setOnClickListener {
            floatingButtonClicked(it)
        }
    }


    /*fun goDetailFragment(view: View){
        val action = NotesFragmentDirections.actionNotesFragmentToDetailFragment()
        Navigation.findNavController(view).navigate(action)
    }*/


    fun floatingButtonClicked(view: View){


        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.addItem){
            val action = NotesFragmentDirections.actionNotesFragmentToAddFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
        return true
    }


}