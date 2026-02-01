package com.example.planfollower.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.planfollower.databinding.ItemNotificationBinding
import com.example.planfollower.models.NotificationResponse

class NotificationAdapter(private var notifications: List<NotificationResponse>,
                          private val onItemClick: (NotificationResponse) -> Unit) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notifications[position]
        holder.binding.txtNotificationMessage.text = item.message
        holder.binding.txtNotificationDate.text = item.createdAt

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = notifications.size

    fun updateList(newList: List<NotificationResponse>) {
        notifications = newList
        notifyDataSetChanged()
    }
}