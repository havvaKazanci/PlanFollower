package com.example.planfollower.ui.fragments

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planfollower.ui.adapters.NoteAdapter
import com.example.planfollower.ui.fragments.NotesFragmentDirections
import com.example.planfollower.viewmodels.NotesViewModel
import com.example.planfollower.R
import com.example.planfollower.api.NotificationService
import com.example.planfollower.api.RetrofitClient
import com.example.planfollower.api.TokenManager
import com.example.planfollower.databinding.FragmentNotesBinding
import com.example.planfollower.models.NoteDetail
import com.example.planfollower.repository.NotificationRepository
import com.example.planfollower.utils.SocketHandler
import com.example.planfollower.utils.ViewModelFactory
import com.example.planfollower.viewmodels.NotificationViewModel
import org.json.JSONObject

class NotesFragment : Fragment() , PopupMenu.OnMenuItemClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var noteList : List<NoteDetail>
    private lateinit var popup : PopupMenu
    private val viewModel: NotesViewModel by activityViewModels()
    private lateinit var notificationViewModel: NotificationViewModel
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


        //ui adapter
        adapter = NoteAdapter(emptyList())
        binding.rvNote.adapter = adapter
        binding.rvNote.layoutManager = LinearLayoutManager(requireContext())

        //viewmodel observer
        viewModel.noteList.observe(viewLifecycleOwner) { notes ->
            // used the updateList method created in Adapter for better performance
            adapter.updateList(notes)
        }


        val service = RetrofitClient.createService(NotificationService::class.java)
        val repository = NotificationRepository(service)
        val factory = ViewModelFactory(repository)

        notificationViewModel = ViewModelProvider(this, factory)[NotificationViewModel::class.java]

        notificationViewModel.unreadCount.observe(viewLifecycleOwner) { count ->
            updateNotificationBadge(count)
        }

        // trigger initial data fetch from API
        val token = TokenManager.getToken(requireContext())
        token?.let{ safeToken->
            viewModel.fetchNotes(token)
            notificationViewModel.fetchNotifications(safeToken)


            SocketHandler.setSocket()
            SocketHandler.establishConnection()
            val mSocket = SocketHandler.getSocket()


            val userId = getUserIdFromToken(safeToken)
            userId?.let {id->
                mSocket.emit("register",id)
            }

            setupSocketListeners()

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
                        val action = NotesFragmentDirections.Companion.actionNotesFragmentToAddFragment()
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

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, // dont want drag
            ItemTouchHelper.LEFT // only swipe the right
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false // OnMove is not used

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //taking the position of swiped
                val position = viewHolder.adapterPosition
                val noteToDelete = adapter.getNoteAt(position)

                //delete
                val token = TokenManager.getToken(requireContext())
                if (token != null) {
                    viewModel.deleteNote(token, noteToDelete.id)
                    Toast.makeText(requireContext(), "Note Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //connection to recyclerview rvNote
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvNote)


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //trigger when search
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //when you press any key it will send the request to backend
                val token = TokenManager.getToken(requireContext())
                if (token != null) {
                    viewModel.fetchNotes(token, newText) //sending request when you press any key
                }
                return true
            }
        })

    }

    private fun performLogout() {
        //Remove the JWT token from SharedPreferences to end the session
        TokenManager.clearToken(requireContext())

        //navigate back login fragment for another login process
        // ensure the backstack is cleared ,user cannot return via back button
        val action = NotesFragmentDirections.Companion.actionNotesFragmentToLoginFragment()
        Navigation.findNavController(requireView()).navigate(action)

        //successful process message
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    private fun getUserIdFromToken(token: String): String? {
        return try {

            val parts = token.split(".")
            if (parts.size < 2) return null

            val payload = String(Base64.decode(parts[1], Base64.DEFAULT))
            val jsonObject = JSONObject(payload)


            jsonObject.getString("userId")
        } catch (e: Exception) {
            null
        }
    }

    private fun setupSocketListeners() {
        val mSocket = SocketHandler.getSocket()

        val token = TokenManager.getToken(requireContext())

        mSocket.on("new_notification") { args ->
            if (args[0] != null) {
                val data = args[0] as JSONObject
                val title = data.getString("title")
                val message = data.getString("message")
                val noteId = data.getString("noteId")

                token?.let { safeToken->
                    activity?.runOnUiThread {
                        // toast message for notification for now only
                        Toast.makeText(requireContext(), "$title: $message", Toast.LENGTH_LONG).show()

                        viewModel.fetchNotes(token)
                        notificationViewModel.fetchNotifications(token)

                    }
                }

            }
        }
    }


    private fun updateNotificationBadge(count: Int) {
        if (count > 0) {
            binding.txtNotificationBadge.text = if (count > 9) "9+" else count.toString()
            binding.txtNotificationBadge.visibility = View.VISIBLE
        } else {
            binding.txtNotificationBadge.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        SocketHandler.getSocket().off("new_notification")
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.addItem){
            val action = NotesFragmentDirections.Companion.actionNotesFragmentToAddFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
        return true
    }


}