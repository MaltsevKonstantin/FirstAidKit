package ru.konstantin.maltsev.firstaidkit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.konstantin.maltsev.firstaidkit.R
import ru.konstantin.maltsev.firstaidkit.databinding.ItemDateNumberBinding

class SelectNumberAdapter(startNumber: Int, endNumber: Int):
    RecyclerView.Adapter<SelectNumberHolder>() {

    private val numbers = ArrayList<Int>()
    var currentPosition = 0

    init {
        for (i in endNumber downTo startNumber) {
            numbers.add(i)
        }
    }

    fun getIndex(number: Int): Int {
        for (i in numbers.indices) {
            if (numbers[i] == number) return i
        }
        return -1
    }

    fun getSelectedNumber(): Int {
        return numbers[currentPosition]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectNumberHolder {
        return SelectNumberHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_date_number, parent, false))
    }

    override fun onBindViewHolder(holder: SelectNumberHolder, position: Int) {
        holder.bind(numbers[position])
        currentPosition = position
    }

    override fun getItemCount(): Int {
        return numbers.size
    }
}

class SelectNumberHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemDateNumberBinding.bind(itemView)

    fun bind(number: Int) {
        binding.name.text = number.toString()
    }
}