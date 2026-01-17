package com.example.planfollower

import android.R.id.list
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planfollower.api.TokenManager
import com.example.planfollower.databinding.FragmentNotesBinding


class NotesFragment : Fragment() , PopupMenu.OnMenuItemClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var noteList : List<NoteDetail>
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

        adapter = NoteAdapter(emptyList())
        binding.rvNote.adapter = adapter
        binding.rvNote.layoutManager = LinearLayoutManager(requireContext())


        viewModel.noteList.observe(viewLifecycleOwner) { notes ->
            // used the updateList method created in Adapter for better performance
            adapter.updateList(notes)
        }

        // trigger initial data fetch from API
        val token = TokenManager.getToken(requireContext())
        if (token != null) {
            viewModel.fetchNotes(token)
        }


        binding.floatingActionButton.setOnClickListener { view ->
            // creating popup menu
            val popupMenu = PopupMenu(requireContext(), view)

            // inflate the menu resource into the popupmenu
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            //setting click listener
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.addItem -> {
                        //navigation to related page
                        val action = NotesFragmentDirections.actionNotesFragmentToAddFragment()
                        Navigation.findNavController(requireView()).navigate(action)
                        true
                    }
                    R.id.logoutItem -> {
                        //calling function which triggers to logout
                        performLogout()
                        true
                    }
                    else -> false
                }
            }
            //display popup menu
            popupMenu.show()
        }

    }

    private fun performLogout() {
        //Remove the JWT token from SharedPreferences to end the session
        TokenManager.clearToken(requireContext())

        //navigate back login fragment for another login process
        // ensure the backstack is cleared ,user cannot return via back button
        val action = NotesFragmentDirections.actionNotesFragmentToLoginFragment()
        Navigation.findNavController(requireView()).navigate(action)

        //successful process message
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
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