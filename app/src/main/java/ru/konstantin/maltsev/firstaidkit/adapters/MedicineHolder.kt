package ru.konstantin.maltsev.firstaidkit.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.konstantin.maltsev.firstaidkit.R
import ru.konstantin.maltsev.firstaidkit.databinding.ItemMedicineBinding
import ru.konstantin.maltsev.firstaidkit.objects.Medicine
import java.text.SimpleDateFormat
import java.util.*

class MedicineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemMedicineBinding.bind(itemView)

    @SuppressLint("UseCompatTextViewDrawableApis")
    fun bind(medicine : Medicine) = with(binding) {
        medicineName.text = medicine.name

        val df = SimpleDateFormat("MM.yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = medicine.manufactureTimestamp
        val sManufactureDate = "Дата изг. " + df.format(calendar.time)
        manufactureDate.text = sManufactureDate

        calendar.timeInMillis = medicine.expirationTimestamp
        val sExpirationDate = "Дата оконч. " + df.format(calendar.time)
        expirationDate.text = sExpirationDate

        lateinit var remainingShelfLife : String
        var isExpired = false
        if (medicine.isExpirationDateHasExpired()) {
            remainingShelfLife = "Срок годности истек"
            isExpired = true
            progressDate.visibility = View.GONE
        } else {
            progressDate.max = medicine.getDeferenceDays()
            progressDate.progress = medicine.getRemainingDays()
            progressDate.visibility = View.VISIBLE
            remainingShelfLife = medicine.getStringRemainingShelfLife()

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (medicine.isDanger) {
                    progressDate.progressTintList = ColorStateList.valueOf(
                        progressDate.context.getColor(R.color.danger)
                    )
                } else if (medicine.isWarning) {
                    progressDate.progressTintList = ColorStateList.valueOf(
                        progressDate.context.getColor(R.color.warning)
                    )
                } else {
                    progressDate.progressTintList = ColorStateList.valueOf(
                        progressDate.context.getColor(R.color.good)
                    )
                }
            }
        }
        message.text = remainingShelfLife
        if (isExpired) {
            message.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_warning_24,
                0,
                0,
                0
            )
            message.compoundDrawablePadding = 4
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                message.compoundDrawableTintList = ColorStateList.valueOf(
                    message.context.getColor(R.color.danger)
                )
        } else {
            message.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }
}