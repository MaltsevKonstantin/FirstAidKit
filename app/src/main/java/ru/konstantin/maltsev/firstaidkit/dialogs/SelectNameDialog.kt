package ru.konstantin.maltsev.firstaidkit.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.konstantin.maltsev.firstaidkit.adapters.NameHolder
import ru.konstantin.maltsev.firstaidkit.adapters.SelectNameAdapter
import ru.konstantin.maltsev.firstaidkit.databinding.DialogSelectNameBinding
import ru.konstantin.maltsev.firstaidkit.interfaces.OnSelectNameListener

class SelectNameDialog(var names: ArrayList<NameHolder>, var listener: OnSelectNameListener): DialogFragment() {

    companion object {
        const val TAG = "SelectNameDialog"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogSelectNameBinding.inflate(layoutInflater)

        val adapter = SelectNameAdapter(names, object: OnSelectNameListener {
            override fun onSelectNameResponse(name: NameHolder) {
                listener.onSelectNameResponse(name)
                dismiss()
            }
        })

        binding.namesRecycler.adapter = adapter
        binding.namesRecycler.layoutManager = LinearLayoutManager(context)
        binding.namesRecycler.setHasFixedSize(true)
        binding.namesRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))

        val builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)
        return builder.create()
    }
}