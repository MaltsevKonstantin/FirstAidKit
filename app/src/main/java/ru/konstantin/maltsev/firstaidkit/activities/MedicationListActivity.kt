package ru.konstantin.maltsev.firstaidkit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import ru.konstantin.maltsev.firstaidkit.R
import ru.konstantin.maltsev.firstaidkit.adapters.MedicationListAdapter
import ru.konstantin.maltsev.firstaidkit.databinding.ActivityMedicationListBinding
import ru.konstantin.maltsev.firstaidkit.objects.Medicine
import ru.konstantin.maltsev.firstaidkit.realm.Configuration

class MedicationListActivity : AppCompatActivity() {

    lateinit var binding: ActivityMedicationListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMedicationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddMedicine.setOnClickListener{
            startActivity(Intent(this, MedicineActivity::class.java))
        }

        val adapter = MedicationListAdapter()
        binding.medicationRecycler.adapter = adapter
        binding.medicationRecycler.layoutManager = LinearLayoutManager(this)
        binding.medicationRecycler.setHasFixedSize(true)

        adapter.listener = object : MedicationListAdapter.OnSelectMedicineListener {
            override fun onSelectMedicine(medicine: Medicine) {
                val intent = Intent(this@MedicationListActivity, MedicineActivity::class.java)
                intent.putExtra(MedicineActivity.MEDICINE_ID, medicine.id)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val adapter = binding.medicationRecycler.adapter as MedicationListAdapter
        adapter.update()
    }
}