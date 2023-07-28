package com.dkgtech.classicon

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dkgtech.classicon.databinding.NoteRowBinding

class RecyclerNoteAdapter(val context: Context, val arrNote: ArrayList<NoteModel>) :
    RecyclerView.Adapter<RecyclerNoteAdapter.ViewHolder>() {
    class ViewHolder(val binding: NoteRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(noteModel: NoteModel) {
            binding.note = noteModel
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NoteRowBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return arrNote.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtIndex.text = "${position + 1}"
        holder.bind(arrNote[position])
    }
}