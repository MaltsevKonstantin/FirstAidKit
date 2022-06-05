package ru.konstantin.maltsev.firstaidkit.objects

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import java.util.*

class Medicine : RealmObject{
    @PrimaryKey var id : Long = 0
    var name : String = ""
    var manufacturerId : Long = 0
    var groupId : Long = 0
    var manufactureTimestamp : Long = 0
    var expirationTimestamp : Long = 0

    @Ignore var isDanger = false
    @Ignore var isWarning = false

    fun isExpirationDateHasExpired() : Boolean {
        val expirationCalendar = Calendar.getInstance()
        expirationCalendar.timeInMillis = expirationTimestamp
        return Calendar.getInstance() > expirationCalendar
    }

    fun getDeferenceDays() : Int {
        return ((expirationTimestamp - manufactureTimestamp) / 86400000).toInt()
    }

    fun getRemainingDays() : Int {
        return ((expirationTimestamp - Calendar.getInstance().timeInMillis) / 86400000).toInt()
    }

    fun getStringRemainingShelfLife(): String {
        var remainingShelfLife = "Оставшийся срок годности: "
        val calendar = Calendar.getInstance()
        val remainingMillisecond: Long =
            (expirationTimestamp - calendar.timeInMillis) / 1000
        //2592000 = 30 days
        if (remainingMillisecond < 2592000L) {
            //86400 = 1 day
            val day = remainingMillisecond.toInt() / 86400
            remainingShelfLife += if (day == 1 || day == 21 || day == 31) "$day день"
                else if (day in 2..4 || day in 22..24) "$day дня"
                else "$day дней"
            isDanger = true
        } else {
            val expirationCalendar = Calendar.getInstance()
            expirationCalendar.timeInMillis = expirationTimestamp
            var year: Int = expirationCalendar.get(Calendar.YEAR) - calendar[Calendar.YEAR]
            var month: Int = expirationCalendar.get(Calendar.MONTH) - calendar[Calendar.MONTH]
            if (month < 0) {
                year--
                month += 12
            }
            if (year > 0) {
                remainingShelfLife += if (year < 2) "$year год "
                    else if (year < 5) "$year года "
                    else "$year лет "
            }
            if (month > 0) {
                remainingShelfLife += if (month < 2) "$month месяц "
                    else if (month < 5) "$month месяца "
                    else "$month месяцев "
            }
            if (year == 0 && month < 4) isWarning = true
        }
        return remainingShelfLife
    }

    fun copy(): Medicine {
        val medicine = Medicine()
        medicine.id = this.id
        medicine.name = this.name
        medicine.manufacturerId = this.manufacturerId
        medicine.groupId = this.groupId
        medicine.manufactureTimestamp = this.manufactureTimestamp
        medicine.expirationTimestamp = this.expirationTimestamp
        return medicine
    }
}