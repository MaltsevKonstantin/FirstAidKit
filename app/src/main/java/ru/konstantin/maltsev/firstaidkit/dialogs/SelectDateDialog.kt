package ru.konstantin.maltsev.firstaidkit.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import ru.konstantin.maltsev.firstaidkit.adapters.SelectNumberAdapter
import ru.konstantin.maltsev.firstaidkit.databinding.DialogSelectDateBinding

class SelectDateDialog(var month: Int, var year: Int, var listener: OnSelectDateListener): DialogFragment() {

    companion object {
        const val TAG = "SelectDateDialog"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogSelectDateBinding.inflate(layoutInflater)

        val monthAdapter = SelectNumberAdapter(1, 12)
        val monthIndex = monthAdapter.getIndex(month)

        val yearAdapter = SelectNumberAdapter(year - 10, year + 10)
        val yearIndex = yearAdapter.getIndex(year)

        binding.monthRecycler.adapter = monthAdapter
        binding.monthRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        binding.monthRecycler.setHasFixedSize(true)
        binding.monthRecycler.scrollToPosition(monthIndex)
        LinearSnapHelper().attachToRecyclerView(binding.monthRecycler)

        binding.yearRecycler.adapter = yearAdapter
        binding.yearRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        binding.yearRecycler.setHasFixedSize(true)
        binding.yearRecycler.scrollToPosition(yearIndex)
        LinearSnapHelper().attachToRecyclerView(binding.yearRecycler)

        binding.btnSave.setOnClickListener {
            listener.onSelectDate(monthAdapter.getSelectedNumber(), yearAdapter.getSelectedNumber())
        }

        return AlertDialog.Builder(activity).setView(binding.root).create()
    }

}

interface OnSelectDateListener {
    fun onSelectDate(month: Int, year: Int)
}