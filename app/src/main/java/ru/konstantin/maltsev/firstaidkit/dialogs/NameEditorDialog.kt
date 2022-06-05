package ru.konstantin.maltsev.firstaidkit.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import ru.konstantin.maltsev.firstaidkit.databinding.DialogEditorNameBinding
import ru.konstantin.maltsev.firstaidkit.databinding.ItemGroupAndManufBinding

class NameEditorDialog(val type: Int, val id: Long, var name: String, private val listener: OnNameEditorResponseListener): DialogFragment() {

    interface OnNameEditorResponseListener {
        fun onNameEditorResponse(type: Int, id: Long, name: String)
    }

    companion object {
        const val TAG = "NameEditorDialog"
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogEditorNameBinding.inflate(layoutInflater)
        binding.title.text = if (type == 0) "Производитель" else "Группа"
        if (id != 0L) binding.editName.setText(name)

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener {
            if (binding.editName.length() < 1) Snackbar.make(binding.editName, "Нужно ввести имя", Snackbar.LENGTH_SHORT).show()
            else {
                if (binding.editName.text.toString().replace(" ", "").isEmpty())
                    Snackbar.make(binding.editName, "Нужно ввести имя", Snackbar.LENGTH_SHORT).show()
                else {
                    listener.onNameEditorResponse(type, id, binding.editName.text.toString())
                    dismiss()
                }
            }
        }

        return AlertDialog.Builder(activity).setView(binding.root).create()
    }
}