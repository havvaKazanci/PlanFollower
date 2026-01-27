package com.example.planfollower.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.planfollower.viewmodels.NotesViewModel
import com.example.planfollower.api.RetrofitClient
import com.example.planfollower.api.TokenManager
import com.example.planfollower.databinding.FragmentAddBinding
import com.example.planfollower.models.NoteRequest
import kotlinx.coroutines.launch

class AddFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveNote.setOnClickListener {
            saveNoteToBackend()
        }
    }

    private fun saveNoteToBackend() {
        val title = binding.etNoteTitle.text.toString().trim()
        val content = binding.etNoteDetail.text.toString().trim()

        // validate inputs before making the API call
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val request = NoteRequest(title, content)

        // retrieve the stored JWT token from TokenManager
        val savedToken = TokenManager.getToken(requireContext())

        // ensure the token is not null before sending the request
        if (savedToken != null) {
            // add the "Bearer " prefix required by the backend middleware
            val authHeader = "Bearer $savedToken"

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.createNote(authHeader, request)

                    if (response.isSuccessful) {
                        //success process inform user and navigate back to the list
                        Toast.makeText(requireContext(), "Note saved successfully!", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp() // returns to the previous fragment
                    } else {
                        //error
                        val errorMsg = response.errorBody()?.string()
                        Log.e("PlanFollower", "Save Note Error: $errorMsg")
                        Toast.makeText(requireContext(), "Failed to save note", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // connectivity problems
                    Log.e("PlanFollower", "Connection Error: ${e.message}")
                    Toast.makeText(requireContext(), "Check your internet connection", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // if token is missing, redirect to login might be needed
            Toast.makeText(requireContext(), "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}