package com.example.planfollower

import android.app.Notification
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.planfollower.databinding.FragmentNotesBinding
import com.example.planfollower.databinding.ReyclerRowBinding

class NoteAdapter(val noteList: ArrayList<Note>): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    class NoteViewHolder(val binding: ReyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ReyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }



    override fun getItemCount(): Int {
        return noteList.size
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.binding.tvTitle.text= noteList[position].title
        holder.binding.tvDesc.text= noteList[position].noteDetail


        holder.itemView.setOnClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToDetailFragment(noteList[position])
            Navigation.findNavController(it).navigate(action)
        }


    }

}