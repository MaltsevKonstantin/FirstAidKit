package ru.konstantin.maltsev.firstaidkit.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.konstantin.maltsev.firstaidkit.databinding.DialogSelectDate2Binding
import ru.konstantin.maltsev.firstaidkit.interfaces.OnSelectDateListener

class SelectDate2Dialog(var month: Int, var year: Int, var listener: ru.konstantin.maltsev.firstaidkit.dialogs.OnSelectDateListener): DialogFragment() {

    companion object {
        const val TAG = "SelectDateDialog"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogSelectDate2Binding.inflate(layoutInflater)

        var month = month
        var year = year
        val minYear = year - 10
        val maxYear = year + 10

        binding.month.text = month.toString()
        binding.year.text = year.toString()

        binding.btnUpMonth.setOnClickListener {
            month++
            if (month > 12) month = 1
            binding.month.text = month.toString()
        }

        binding.btnDownMonth.setOnClickListener {
            month--
            if (month < 1) month = 12
            binding.month.text = month.toString()
        }

        binding.btnUpYear.setOnClickListener {
            year++
            if (year > maxYear) year = minYear
            binding.year.text = year.toString()
        }

        binding.btnDownYear.setOnClickListener {
            year--
            if (year < minYear) year = maxYear
            binding.year.text = year.toString()
        }

        binding.btnSave.setOnClickListener {
            listener.onSelectDate(month, year)
            dismiss()
        }

        val dialog = AlertDialog.Builder(activity).setView(binding.root).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}