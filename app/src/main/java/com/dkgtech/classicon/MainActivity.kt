package com.dkgtech.classicon

import android.app.Dialog
import android.graphics.drawable.GradientDrawable.Orientation
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dkgtech.classicon.databinding.ActivityMainBinding
import com.dkgtech.classicon.databinding.AddNoteDialogBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var firestoreDB: FirebaseFirestore

    val arrNotes = ArrayList<NoteModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            firestoreDB = Firebase.firestore


//            add data

            floatingActionButton.setOnClickListener {
                val dialog = Dialog(this@MainActivity)
                val dialogBinding = AddNoteDialogBinding.inflate(layoutInflater)
                dialog.setContentView(dialogBinding.root)

                with(dialogBinding) {
                    btnAdd.setOnClickListener {
                        val title = edtTitle.text.toString()
                        val desc = edtDesc.text.toString()

                        if (title != "" && desc != "") {
                            val noteModel = NoteModel(title, desc)
                            firestoreDB
                                .collection("notes")
                                .add(noteModel)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Note Added Successfully",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    dialog.dismiss()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Note not added",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                        }

                    }
                }
                dialog.show()
            }


//            read data

            firestoreDB
                .collection("notes")
                .get()
                .addOnSuccessListener {
                    for (doc in it.documents) {

                        val noteModel = doc.toObject(NoteModel::class.java)
                        noteModel?.let {
                            arrNotes.add(noteModel)
                        }

                    }

//                    set recycler view

                    rcViewNotes.layoutManager = LinearLayoutManager(this@MainActivity)
                    rcViewNotes.adapter = RecyclerNoteAdapter(this@MainActivity, arrNotes)

                }


        }

    }
}