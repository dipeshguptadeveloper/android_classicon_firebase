package com.dkgtech.classicon

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dkgtech.classicon.databinding.ActivityMainBinding
import com.dkgtech.classicon.databinding.AddNoteDialogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var firestoreDB: FirebaseFirestore

    lateinit var auth: FirebaseAuth

    val arrNotes = ArrayList<NoteModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreDB = Firebase.firestore

        auth = Firebase.auth

        with(binding) {


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
                            noteModel.docId = doc.id
                            arrNotes.add(noteModel)
                        }

                    }

//                    set recycler view

                    rcViewNotes.layoutManager = LinearLayoutManager(this@MainActivity)
                    rcViewNotes.adapter = RecyclerNoteAdapter(this@MainActivity, arrNotes)

                }
        }
    }

    fun updateNote(noteModel: NoteModel) {
        val dialog = Dialog(this@MainActivity)
        val dialogBinding = AddNoteDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        with(dialogBinding) {
            noteTitle.text = "Update Note"
            edtTitle.setText(noteModel.title)
            edtDesc.setText(noteModel.desc)
            btnAdd.text = "Save"
        }

        with(dialogBinding) {
            btnAdd.setOnClickListener {

            }
        }
        dialog.show()
    }

    fun deleteNote(docId: String) {
        val deleteDialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete?")
            .setCancelable(false)
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(
                        this@MainActivity,
                        "Note Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    firestoreDB
                        .collection("notes")
                        .document(docId)
                        .delete()
                }
            })

            .setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                }
            })

        deleteDialog.show()

    }

}