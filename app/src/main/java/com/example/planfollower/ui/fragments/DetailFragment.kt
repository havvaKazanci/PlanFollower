package com.example.planfollower.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.planfollower.ui.fragments.DetailFragmentArgs
import com.example.planfollower.viewmodels.NotesViewModel
import com.example.planfollower.api.TokenManager
import com.example.planfollower.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = DetailFragmentArgs.Companion.fromBundle(requireArguments())
        val note = args.note

        binding.tvTitledetail.setText(note.title)
        binding.tvDescdetail.setText(note.content)

        binding.btnUpdate.setOnClickListener {
            val updatedTitle = binding.tvTitledetail.text.toString().trim()
            val updatedContent = binding.tvDescdetail.text.toString().trim()

            //no blank part
            if (updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
                performUpdate(note.id, updatedTitle, updatedContent)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnShare.setOnClickListener {
            val input = EditText(requireContext())
            input.hint = "E mail"

            AlertDialog.Builder(requireContext())
                .setTitle("Share Note")
                .setMessage("Please write the email you want to share:")
                .setView(input)
                .setPositiveButton("Share") { _, _ ->
                    val email = input.text.toString()
                    if (email.isNotEmpty()) {
                        val token = TokenManager.getToken(requireContext())
                        if (token != null) {
                            // ViewModel çağrısı
                            viewModel.shareNote(token, args.note.id, email)
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        //see the result
        viewModel.shareResult.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun performUpdate(noteId: String, title: String, content: String) {
        val token = TokenManager.getToken(requireContext())
        if (token != null) {
            //backend call
            viewModel.updateNote(token, noteId, title, content)

            Toast.makeText(requireContext(), "Note updated successfully", Toast.LENGTH_SHORT).show()

            //back to list when everything ok
            Navigation.findNavController(requireView()).popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}