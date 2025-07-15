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

        recyclerView = view.findViewById(R.id.job_title_recycler)
        searchInput = view.findViewById(R.id.job_search_input)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        jobTitleAdapter = JobTitleAdapter()
        recyclerView.adapter = jobTitleAdapter

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
        jobTitleDao = db.jobTitleDao()

        // Setup expand/collapse functionality for header
        val jobTitleHeader = view.findViewById<View>(R.id.job_title_header)
        val jobTitleContent = view.findViewById<View>(R.id.job_title_content)
        val jobTitleArrow = view.findViewById<ImageView>(R.id.job_title_arrow)
        val jobTitleText = view.findViewById<TextView>(R.id.job_title_text)

        var isExpanded = false
        jobTitleHeader.setOnClickListener {
            isExpanded = !isExpanded
            jobTitleContent.visibility = if (isExpanded) View.VISIBLE else View.GONE

            if (isExpanded) {
                jobTitleHeader.setBackgroundResource(R.drawable.rounded_expandable_bg)
                jobTitleText.setTextColor(resources.getColor(android.R.color.white, null))
                jobTitleArrow.setColorFilter(resources.getColor(android.R.color.white, null))
            } else {
                jobTitleHeader.setBackgroundResource(R.drawable.job_title_header_bg)
                jobTitleText.setTextColor(resources.getColor(R.color.primary, null))
                jobTitleArrow.setColorFilter(resources.getColor(R.color.primary, null))
            }

            val targetRotation = if (isExpanded) 90f else 0f
            jobTitleArrow.animate()
                .rotation(targetRotation)
                .setDuration(200)
                .start()
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
                    // Clear results when search is empty
                    jobTitleAdapter.setFullList(emptyList())
                } else {
                    // Launch coroutine to search DB
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
