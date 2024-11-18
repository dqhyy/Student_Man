package com.example.student_man

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var students: MutableList<StudentModel>
    private var removedStudent: StudentModel? = null
    private var removedPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        students = mutableListOf(
            StudentModel("Nguyễn Văn An", "SV001"),
            StudentModel("Trần Thị Bảo", "SV002"),
            StudentModel("Lê Hoàng Cường", "SV003")
        )
        studentAdapter = StudentAdapter(students, ::onEditClicked, ::onDeleteClicked)

        findViewById<RecyclerView>(R.id.recycler_view_students).apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            showAddStudentDialog()
        }
    }

    private fun showAddStudentDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null)
        val editName = dialogView.findViewById<EditText>(R.id.edit_student_name)
        val editId = dialogView.findViewById<EditText>(R.id.edit_student_id)

        AlertDialog.Builder(this)
            .setTitle("Add New Student")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = editName.text.toString().trim()
                val id = editId.text.toString().trim()
                if (name.isNotEmpty() && id.isNotEmpty()) {
                    students.add(StudentModel(name, id))
                    studentAdapter.notifyItemInserted(students.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onEditClicked(position: Int) {
        val student = students[position]
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null)
        val editName = dialogView.findViewById<EditText>(R.id.edit_student_name)
        val editId = dialogView.findViewById<EditText>(R.id.edit_student_id)

        editName.setText(student.studentName)
        editId.setText(student.studentId)

        AlertDialog.Builder(this)
            .setTitle("Edit Student")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                student.studentName = editName.text.toString().trim()
                student.studentId = editId.text.toString().trim()
                studentAdapter.notifyItemChanged(position)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onDeleteClicked(position: Int) {
        val student = students[position]

        AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.studentName}?")
            .setPositiveButton("Yes") { _, _ ->
                removedStudent = student
                removedPosition = position
                students.removeAt(position)
                studentAdapter.notifyItemRemoved(position)

                Snackbar.make(findViewById(R.id.main), "${student.studentName} deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        students.add(removedPosition, removedStudent!!)
                        studentAdapter.notifyItemInserted(removedPosition)
                    }
                    .show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}
