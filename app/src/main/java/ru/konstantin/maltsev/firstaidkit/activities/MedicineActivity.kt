package ru.konstantin.maltsev.firstaidkit.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.query
import ru.konstantin.maltsev.firstaidkit.R
import ru.konstantin.maltsev.firstaidkit.adapters.NameHolder
import ru.konstantin.maltsev.firstaidkit.databinding.ActivityMedicineBinding
import ru.konstantin.maltsev.firstaidkit.dialogs.*
import ru.konstantin.maltsev.firstaidkit.interfaces.OnSelectNameListener
import ru.konstantin.maltsev.firstaidkit.objects.Group
import ru.konstantin.maltsev.firstaidkit.objects.Manufacturer
import ru.konstantin.maltsev.firstaidkit.objects.Medicine
import ru.konstantin.maltsev.firstaidkit.realm.Configuration
import java.text.SimpleDateFormat
import java.util.*

class MedicineActivity : AppCompatActivity() {

    companion object {
        const val MEDICINE_ID = "id"
    }

    lateinit var binding : ActivityMedicineBinding
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realm = Realm.open(Configuration().getDefault())

        intent
        val medicineId = intent.getLongExtra(MEDICINE_ID, 0)
        val medicine = if (medicineId == 0L) Medicine()
            else realm.query<Medicine>("id == $medicineId").find()[0].copy()

        if (medicineId != 0L) {
            binding.editMedicineName.setText(medicine.name)

            if (medicine.manufacturerId != 0L) {
                val manufacturer = realm.query<Manufacturer>("id == ${medicine.manufacturerId}").find()[0]
                binding.selectedManufacturer.text = manufacturer.name
            }

            if (medicine.groupId != 0L) {
                val group = realm.query<Group>("id == ${medicine.groupId}").find()[0]
                binding.selectedGroup.text = group.name
            }

            updateManufacturedDate(medicine)
            updateExpirationDate(medicine)
        }

        binding.fabAddManufacturer.setOnClickListener {
            createNewManufactured(medicine)
        }

        binding.fabAddGroup.setOnClickListener {
            createNewGroup(medicine)
        }

        binding.selectedManufacturer.setOnClickListener {
            val realmResults = realm.query<Manufacturer>().find()
            if (realmResults.size == 0) createNewManufactured(medicine)
            else {
                val names = ArrayList<NameHolder>()
                realmResults.forEach {
                    val name = NameHolder()
                    name.id = it.id
                    name.name = it.name
                    names.add(name)
                }
                SelectNameDialog(names, object : OnSelectNameListener {
                    override fun onSelectNameResponse(name: NameHolder) {
                        medicine.manufacturerId = name.id
                        binding.selectedManufacturer.text = name.name
                    }
                }).show(supportFragmentManager, SelectNameDialog.TAG)
            }
        }

        binding.selectedGroup.setOnClickListener {
            val realmResults = realm.query<Group>().find()
            if (realmResults.size == 0) createNewGroup(medicine)
            else {
                val names = ArrayList<NameHolder>()
                realmResults.forEach {
                    val name = NameHolder()
                    name.id = it.id
                    name.name = it.name
                    names.add(name)
                }
                SelectNameDialog(names, object : OnSelectNameListener {
                    override fun onSelectNameResponse(name: NameHolder) {
                        medicine.groupId = name.id
                        binding.selectedGroup.text = name.name
                    }
                }).show(supportFragmentManager, SelectNameDialog.TAG)
            }
        }

        binding.manufactureDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (medicine.manufactureTimestamp > 0) calendar.timeInMillis = medicine.manufactureTimestamp
            SelectDate2Dialog(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR),
            object : OnSelectDateListener {
                override fun onSelectDate(month: Int, year: Int) {
                    calendar.set(Calendar.MONTH, month - 1)
                    calendar.set(Calendar.YEAR, year)
                    medicine.manufactureTimestamp = calendar.timeInMillis
                    updateManufacturedDate(medicine)
                }
            }).show(supportFragmentManager, SelectDate2Dialog.TAG)
        }

        binding.expirationDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (medicine.expirationTimestamp > 0) calendar.timeInMillis = medicine.expirationTimestamp
            SelectDate2Dialog(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR),
                object : OnSelectDateListener {
                    override fun onSelectDate(month: Int, year: Int) {
                        calendar.set(Calendar.MONTH, month - 1)
                        calendar.set(Calendar.YEAR, year)
                        medicine.expirationTimestamp = calendar.timeInMillis
                        updateExpirationDate(medicine)
                    }
                }).show(supportFragmentManager, SelectDate2Dialog.TAG)
        }

        binding.fabSaveMedicine.setOnClickListener {
            if (binding.editMedicineName.text.isEmpty() || binding.editMedicineName.text.toString().replace(" ", "").isEmpty()) {
                showWarning("Введите наименование лекарства.")
                return@setOnClickListener
            }

            medicine.name = binding.editMedicineName.text.toString()

            if (medicine.manufacturerId == 0L) {
                showWarning("Выберите производителя.")
                return@setOnClickListener
            }

            if (medicine.groupId == 0L) {
                showWarning("Выберите группу.")
                return@setOnClickListener
            }

            if (medicine.manufactureTimestamp == 0L || medicine.expirationTimestamp == 0L) {
                showWarning("Установите дату производства и дату окончания срока годности.")
                return@setOnClickListener
            }

            if (medicine.manufactureTimestamp > medicine.expirationTimestamp) {
                showWarning("Дата изготовления не может быть меньше даты окончания срока годности.")
                return@setOnClickListener
            }

            if (medicine.id == 0L) {
                medicine.id = realm.query<Medicine>().find().size.toLong() + 1
                realm.writeBlocking { copyToRealm(medicine) }
                finish()
            } else {
                realm.writeBlocking {
                    val recordedMedicine = findLatest(query<Medicine>("id == ${medicine.id}").find()[0])
                    recordedMedicine?.name = medicine.name
                    recordedMedicine?.manufacturerId = medicine.manufacturerId
                    recordedMedicine?.groupId = medicine.groupId
                    recordedMedicine?.manufactureTimestamp = medicine.manufactureTimestamp
                    recordedMedicine?.expirationTimestamp = medicine.expirationTimestamp
                }
                finish()
            }
            showWarning("Произошла ошибка.")
        }
    }

    private fun showWarning(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbar.setBackgroundTint(getColor(R.color.red_A700))
        }
        snackbar.show()
    }

    private fun createNewManufactured(medicine: Medicine) {
        NameEditorDialog(0, medicine.manufacturerId, "",
            object : NameEditorDialog.OnNameEditorResponseListener {
                override fun onNameEditorResponse(type: Int, id: Long, name: String) {
                    val manufacturer = Manufacturer()
                    manufacturer.name = name
                    manufacturer.id = (realm.query<Manufacturer>().find().size + 1).toLong()
                    realm.writeBlocking { copyToRealm(manufacturer) }
                    medicine.manufacturerId = manufacturer.id
                    binding.selectedManufacturer.text = manufacturer.name
                }
            }).show(supportFragmentManager, NameEditorDialog.TAG)
    }

    private fun createNewGroup(medicine: Medicine) {
        NameEditorDialog(1, medicine.manufacturerId, "",
            object : NameEditorDialog.OnNameEditorResponseListener {
                override fun onNameEditorResponse(type: Int, id: Long, name: String) {
                    val group = Group()
                    group.name = name
                    group.id = (realm.query<Group>().find().size + 1).toLong()
                    realm.writeBlocking { copyToRealm(group) }
                    medicine.groupId = group.id
                    binding.selectedGroup.text = group.name
                }
            }).show(supportFragmentManager, NameEditorDialog.TAG)
    }

    fun updateManufacturedDate(medicine: Medicine) {
        val df = SimpleDateFormat("MM.yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = medicine.manufactureTimestamp
        binding.manufactureDate.text = df.format(calendar.time)
    }

    fun updateExpirationDate(medicine: Medicine) {
        val df = SimpleDateFormat("MM.yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = medicine.expirationTimestamp
        binding.expirationDate.text = df.format(calendar.time)
    }
}