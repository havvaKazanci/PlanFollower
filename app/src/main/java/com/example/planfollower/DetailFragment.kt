package com.example.planfollower

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.planfollower.api.TokenManager
import com.example.planfollower.databinding.FragmentAddBinding
import com.example.planfollower.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by activityViewModels()

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

        val args = DetailFragmentArgs.fromBundle(requireArguments())
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