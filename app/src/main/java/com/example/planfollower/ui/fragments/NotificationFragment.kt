package com.example.planfollower.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planfollower.R
import com.example.planfollower.api.NotificationService
import com.example.planfollower.api.RetrofitClient
import com.example.planfollower.databinding.FragmentNotesBinding
import com.example.planfollower.databinding.FragmentNotificationBinding
import com.example.planfollower.repository.NotificationRepository
import com.example.planfollower.ui.adapters.NotificationAdapter
import com.example.planfollower.utils.ViewModelFactory
import com.example.planfollower.viewmodels.NotificationViewModel


class NotificationFragment : Fragment() {


    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by activityViewModels {
        val service = RetrofitClient.createService(NotificationService::class.java)
        ViewModelFactory(NotificationRepository(service))
    }

    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NotificationAdapter(emptyList())
        binding.rvNotifications.adapter = adapter
        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())


        viewModel.unreadNotifications.observe(viewLifecycleOwner) { list ->

            adapter.updateList(list)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}