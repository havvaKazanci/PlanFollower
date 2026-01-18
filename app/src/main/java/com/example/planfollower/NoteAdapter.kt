package com.example.planfollower

import android.app.Notification
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.planfollower.databinding.FragmentNotesBinding
import com.example.planfollower.databinding.ReyclerRowBinding

class NoteAdapter(private var noteList: List<NoteDetail>): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


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
        val currentNote = noteList[position]

        // mapping backend fields: 'title' and 'noteDetail'
        holder.binding.tvTitle.text = currentNote.title
        holder.binding.tvDesc.text = currentNote.content

        // navigation to DetailFragment passing the full NoteDetail object
        holder.itemView.setOnClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToDetailFragment(currentNote)
            Navigation.findNavController(it).navigate(action)
        }


    }

    fun getNoteAt(position: Int): NoteDetail {
        return noteList[position]
    }

    fun updateList(newList: List<NoteDetail>) {
        this.noteList = newList
        notifyDataSetChanged() // refresh the UI with new data
    }

}