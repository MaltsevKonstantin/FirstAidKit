package ru.konstantin.maltsev.firstaidkit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults
import io.realm.query
import ru.konstantin.maltsev.firstaidkit.R
import ru.konstantin.maltsev.firstaidkit.objects.Medicine
import ru.konstantin.maltsev.firstaidkit.realm.Configuration

class MedicationListAdapter : RecyclerView.Adapter<MedicineHolder>() {

    interface OnSelectMedicineListener {
        fun onSelectMedicine(medicine: Medicine)
    }

    var listener: OnSelectMedicineListener? = null
    private val currentList = ArrayList<Medicine>()
    private val oldList = ArrayList<Medicine>()

    init {
        update()
    }

    fun update() {
        currentList.clear()
        val realmResults = Realm.open(Configuration().getDefault()).query<Medicine>().find()
        realmResults.forEach { currentList.add(it.copy()) }

        val diffUtilCallback = DiffUtilCallback(currentList, oldList)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        result.dispatchUpdatesTo(this)

        oldList.clear()
        currentList.forEach { oldList.add(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineHolder {
        return MedicineHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_medicine, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MedicineHolder, position: Int) {
        val medicine = currentList[position]

        holder.bind(medicine)

        holder.itemView.setOnClickListener {
            listener?.onSelectMedicine(medicine)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    open class DiffUtilCallback(
        private val newList: ArrayList<Medicine>,
        private val oldList: ArrayList<Medicine>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.name == newItem.name && oldItem.manufacturerId == newItem.manufacturerId
                    && oldItem.groupId == newItem.groupId && oldItem.manufactureTimestamp == newItem.manufactureTimestamp
                    && oldItem.expirationTimestamp == newItem.expirationTimestamp
        }
    }
}