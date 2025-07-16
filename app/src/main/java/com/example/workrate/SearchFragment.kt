package com.example.workrate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.workrate.data.AppDatabase
import com.example.workrate.data.JobTitleDao
import kotlinx.coroutines.*

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var jobTitleAdapter: JobTitleAdapter

    private lateinit var db: AppDatabase
    private lateinit var jobTitleDao: JobTitleDao

    private var searchJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // RecyclerView and search
        recyclerView = view.findViewById(R.id.job_title_recycler)
        searchInput = view.findViewById(R.id.job_search_input)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        jobTitleAdapter = JobTitleAdapter()
        recyclerView.adapter = jobTitleAdapter

        // Room database setup
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
        jobTitleDao = db.jobTitleDao()

        // Job Title expand/collapse setup
        val jobTitleHeader = view.findViewById<View>(R.id.job_title_header)
        val jobTitleContent = view.findViewById<View>(R.id.job_title_content)
        val jobTitleArrow = view.findViewById<ImageView>(R.id.job_title_arrow)
        val jobTitleText = view.findViewById<TextView>(R.id.job_title_text)

        var isJobTitleExpanded = false
        jobTitleHeader.setOnClickListener {
            isJobTitleExpanded = !isJobTitleExpanded
            jobTitleContent.visibility = if (isJobTitleExpanded) View.VISIBLE else View.GONE

            if (isJobTitleExpanded) {
                jobTitleHeader.setBackgroundResource(R.drawable.rounded_expandable_bg)
                jobTitleText.setTextColor(resources.getColor(android.R.color.white, null))
                jobTitleArrow.setColorFilter(resources.getColor(android.R.color.white, null))
            } else {
                jobTitleHeader.setBackgroundResource(R.drawable.job_title_header_bg)
                jobTitleText.setTextColor(resources.getColor(R.color.primary, null))
                jobTitleArrow.setColorFilter(resources.getColor(R.color.primary, null))
            }

            val targetRotation = if (isJobTitleExpanded) 90f else 0f
            jobTitleArrow.animate()
                .rotation(targetRotation)
                .setDuration(200)
                .start()
        }

        // LOCATION expand/collapse setup
        val locationHeader = view.findViewById<View>(R.id.location_header)
        val locationContent = view.findViewById<View>(R.id.location_content)
        val locationArrow = view.findViewById<ImageView>(R.id.location_arrow)
        val locationText = view.findViewById<TextView>(R.id.location_text) // Ensure this ID is in your XML

        var isLocationExpanded = false
        locationHeader.setOnClickListener {
            isLocationExpanded = !isLocationExpanded
            locationContent.visibility = if (isLocationExpanded) View.VISIBLE else View.GONE

            if (isLocationExpanded) {
                locationHeader.setBackgroundResource(R.drawable.rounded_expandable_bg)
                locationText.setTextColor(resources.getColor(android.R.color.white, null))
                locationArrow.setColorFilter(resources.getColor(android.R.color.white, null))
            } else {
                locationHeader.setBackgroundResource(R.drawable.job_title_header_bg)
                locationText.setTextColor(resources.getColor(R.color.primary, null))
                locationArrow.setColorFilter(resources.getColor(R.color.primary, null))
            }

            val rotation = if (isLocationExpanded) 90f else 0f
            locationArrow.animate().rotation(rotation).setDuration(200).start()
        }

        setupSearchListener()

        return view
    }

    private fun setupSearchListener() {
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()

                val query = s?.toString()?.trim() ?: ""

                if (query.isEmpty()) {
                    jobTitleAdapter.setFullList(emptyList())
                } else {
                    searchJob = coroutineScope.launch {
                        val results = withContext(Dispatchers.IO) {
                            jobTitleDao.searchJobTitles(query, limit = 50, offset = 0)
                        }
                        jobTitleAdapter.setFullList(results)
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel() // Cancel coroutines to avoid leaks
    }
}
