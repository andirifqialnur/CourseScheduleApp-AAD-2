package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.databinding.ActivityAddCourseBinding
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private var startTime: String = ""
    private var endTime: String = ""
    private lateinit var viewModel: AddCourseViewModel

    private lateinit var binding: ActivityAddCourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_course)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        viewModel.saved.observe(this) {
            if (it.getContentIfNotHandled() == true) {
                onBackPressed()
            } else {
                Toast.makeText(applicationContext, "Time cant be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val courseName = binding.addEdCourseName.text.toString().trim()
                val day = binding.spinnerDay.selectedItemPosition
                val lecturer = binding.addEdLecture.text.toString().trim()
                val note = binding.addEdNote.text.toString().trim()

                viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
                Toast.makeText(applicationContext, "added", Toast.LENGTH_SHORT).show()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    fun showStartTimePicker(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "startPicker")
    }

    fun showEndTimePicker(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "endPicker")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        when (tag) {
            "startPicker" -> {
                binding.addTvStartTime.text = timeFormat.format(calendar.time)
                startTime = timeFormat.format(calendar.time)
            }
            "endPicker" -> {
                binding.addTvEndTime.text = timeFormat.format(calendar.time)
                endTime = timeFormat.format(calendar.time)
            }
        }
    }
}