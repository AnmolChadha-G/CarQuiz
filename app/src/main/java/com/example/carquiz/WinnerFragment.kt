package com.example.carquiz

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carquiz.databinding.FragmentWinnerBinding

class WinnerFragment : Fragment() {

    private lateinit var bundle: Bundle
    private var _binding: FragmentWinnerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWinnerBinding.inflate(inflater, container, false)
        bundle = requireArguments()
        val score = bundle.getString("score")
        binding.editTextTextPersonName2.text = score?.toEditable()
       binding.btnAdd.setOnClickListener {
            addRecord()
           setUpListOfDataIntoRecyclerView()
        }
        setUpListOfDataIntoRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addRecord() {
        val name = binding.editTextTextPersonName.text.toString()
        val score = binding.editTextTextPersonName2.text.toString()
        val databaseHandler: DatabaseHandler? = context?.let { DatabaseHandler(it) }
        if (!name.isEmpty() && !score.isEmpty()) {
            val status = databaseHandler?.addEmployee(EmpModelClass(0, name, score))
            if (status != null) {
                if (status > -1) {
                    Toast.makeText(context, "Record Saved", Toast.LENGTH_SHORT).show()
                    binding.editTextTextPersonName.text.clear()
                    binding.editTextTextPersonName2.text.clear()
                }
            }
        } else {
            Toast.makeText(context, "Name can't be blank", Toast.LENGTH_SHORT).show()
        }
    }

   /* fun updateRecordDialog(empModelClass: EmpModelClass) {
        val updateDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update)

        val etUpdateName = updateDialog.findViewById<EditText>(R.id.etUpdateName)
        etUpdateName?.setText(empModelClass.name)
        val etUpdateScore = updateDialog.findViewById<EditText>(R.id.etUpdateScore)
        etUpdateScore?.setText(empModelClass.score.toString())

        updateDialog.findViewById<Button>(R.id.tvUpdate)?.setOnClickListener {
            val name = etUpdateName?.text.toString()
            val score = etUpdateScore?.text.toString()
            val databaseHandler: DatabaseHandler? = context?.let { DatabaseHandler(it) }
            if (!name.isEmpty() && !score.isEmpty()) {
                val status =
                    databaseHandler?.updateEmployee(EmpModelClass(empModelClass.id, name, score))
                if (status != null) {
                    if (status > -1) {
                        Toast.makeText(context, "Record Saved", Toast.LENGTH_SHORT).show()
                        setUpListOfDataIntoRecyclerView()
                        updateDialog.dismiss()
                    }
                }
            } else {
                Toast.makeText(context, "Name can't be blank", Toast.LENGTH_SHORT).show()
            }
        }

        updateDialog.findViewById<Button>(R.id.tvCancel)?.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }
*/
   fun updateRecordDialog(empModelClass: EmpModelClass) {
       val updateDialog = Dialog(requireContext(), R.style.Theme_Dialog)
       updateDialog.setCancelable(false)
       updateDialog.setContentView(R.layout.dialog_update)

       val etUpdateName = updateDialog.findViewById<EditText>(R.id.etUpdateName)
       val etUpdateScore = updateDialog.findViewById<EditText>(R.id.etUpdateScore)

       etUpdateName?.setText(empModelClass.name)
       etUpdateScore?.setText(empModelClass.score.toString())
       val databaseHandler:DatabaseHandler=DatabaseHandler(requireContext())

       updateDialog.findViewById<Button>(R.id.tvUpdate)?.setOnClickListener(View.OnClickListener {
           val name = etUpdateName?.text.toString().trim()
           val score = etUpdateScore?.text.toString().trim()

           if (name.isNotEmpty() && score.isNotEmpty()) {
               val updatedEmpModel = EmpModelClass(empModelClass.id, name, score)
               val status = databaseHandler.updateEmployee(updatedEmpModel)

               if (status > -1) {
                   Toast.makeText(context, "Record Saved", Toast.LENGTH_SHORT).show()
                   setUpListOfDataIntoRecyclerView()
                   updateDialog.dismiss()
               } else {
                   Toast.makeText(context, "Failed to update record", Toast.LENGTH_SHORT).show()
               }
           } else {
               Toast.makeText(context, "Name or score can't be blank", Toast.LENGTH_SHORT).show()
           }
       })

       updateDialog.findViewById<Button>(R.id.tvCancel)?.setOnClickListener(View.OnClickListener {
           updateDialog.dismiss()
       })
       updateDialog.show()
   }

    private fun String.toEditable(): Editable {
        return SpannableStringBuilder(this)
    }

    fun deleteRecordAlertDialog(empModelClass: EmpModelClass) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Record")
        builder.setMessage("Are you sure you want to Delete ${empModelClass.name}")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            val databaseHandler: DatabaseHandler? = context?.let { DatabaseHandler(it) }
            val status =
                databaseHandler?.deleteEmployee(EmpModelClass(empModelClass.id, "", ""))
            if (status != null) {
                if (status > -1) {
                    Toast.makeText(context, "Record deleted successfully", Toast.LENGTH_SHORT)
                        .show()
                    setUpListOfDataIntoRecyclerView()
                }
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setUpListOfDataIntoRecyclerView() {
        val databaseHandler: DatabaseHandler? = context?.let { DatabaseHandler(it) }
        val itemsList = databaseHandler?.viewEmployee()
        if (itemsList != null) {
            if (itemsList.size > 0) {
                binding.rvItemsList.visibility = View.VISIBLE
                binding.tvNoRecordsAvailable.visibility = View.GONE
                binding.rvItemsList.layoutManager = LinearLayoutManager(requireActivity())
                val itemAdapter = ItemAdapter(this, itemsList)
                binding.rvItemsList.adapter = itemAdapter
            } else {
                binding.rvItemsList.visibility = View.GONE
                binding.tvNoRecordsAvailable.visibility = View.VISIBLE
            }
        }
    }
}
