package ru.konstantin.maltsev.firstaidkit.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnScrollChangeListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import io.realm.Realm
import ru.konstantin.maltsev.firstaidkit.R
import ru.konstantin.maltsev.firstaidkit.adapters.MedicationListAdapter
import ru.konstantin.maltsev.firstaidkit.databinding.ActivityMedicationListBinding
import ru.konstantin.maltsev.firstaidkit.objects.Medicine
import ru.konstantin.maltsev.firstaidkit.realm.Configuration
import java.util.logging.Logger

class MedicationListActivity : AppCompatActivity() {

    lateinit var binding: ActivityMedicationListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMedicationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.light_green_A200_dark)))

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

        binding.medicationRecycler.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) binding.fabAddMedicine.hide()
            else binding.fabAddMedicine.show()
        }
    }

    override fun onResume() {
        super.onResume()
        val adapter = binding.medicationRecycler.adapter as MedicationListAdapter
        adapter.update()
    }
}