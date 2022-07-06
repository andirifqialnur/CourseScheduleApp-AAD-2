package com.dicoding.courseschedule.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.databinding.ActivityAddCourseBinding
import com.dicoding.courseschedule.databinding.ActivityDetailBinding
import com.dicoding.courseschedule.util.DayName.Companion.getByNumber

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val COURSE_ID = "courseId"
    }

    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val courseId = intent.getIntExtra(COURSE_ID, 0)
        val factory = DetailViewModelFactory.createFactory(this, courseId)

        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        viewModel.course.observe(this) { course ->
            showCourseDetail(course)
        }
    }

    private fun showCourseDetail(course: Course?) {
        course?.apply {
            val timeString = getString(R.string.time_format)
            val dayName = getByNumber(day)
            val timeFormat = String.format(timeString, dayName, startTime, endTime)

            binding.tvCourseName.text = course.courseName
            binding.tvTime.text = timeFormat
            binding.tvLecturer.text = course.lecturer
            binding.tvNote.text = course.note
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                AlertDialog.Builder(this).apply {
                    setMessage(getString(R.string.delete_alert))
                    setNegativeButton(getString(R.string.no), null)
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.delete()
                        Toast.makeText(applicationContext, "deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}