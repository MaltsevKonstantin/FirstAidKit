package ru.konstantin.maltsev.firstaidkit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.konstantin.maltsev.firstaidkit.R
import ru.konstantin.maltsev.firstaidkit.databinding.ItemGroupAndManufBinding
import ru.konstantin.maltsev.firstaidkit.interfaces.OnSelectNameListener

class SelectNameAdapter(var names: ArrayList<NameHolder>, var listener: OnSelectNameListener) : RecyclerView.Adapter<SelectNameHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectNameHolder {
        return SelectNameHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_group_and_manuf, parent, false))
    }

    override fun onBindViewHolder(holder: SelectNameHolder, position: Int) {
        val nameHolder = names[position]
        holder.bind(nameHolder)
        holder.itemView.setOnClickListener {listener.onSelectNameResponse(nameHolder)}
    }

    override fun getItemCount(): Int {
        return names.size
    }
}

class SelectNameHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemGroupAndManufBinding.bind(itemView)

    fun bind(nameHolder: NameHolder) {
        binding.name.text = nameHolder.name
    }
}

class NameHolder {
    var id: Long = 0
    var name: String = ""
}