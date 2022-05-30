package ru.konstantin.maltsev.firstaidkit.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.konstantin.maltsev.firstaidkit.R

class MedicationListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medication_list)
    }
}