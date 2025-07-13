package com.example.workrate

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Button at bottom
        val createJobButton = view.findViewById<Button>(R.id.btn_create_job_notification)
        createJobButton.setOnClickListener {
            Toast.makeText(requireContext(), "Create Job Notification clicked!", Toast.LENGTH_SHORT).show()
        }

        // Setup all filters toggle behavior:
        setupFilterToggle(view, R.id.filter_job_title, R.id.job_title_header, R.id.job_title_content, R.id.job_title_arrow)
        setupFilterToggle(view, R.id.filter_location, R.id.location_header, R.id.location_content, R.id.location_arrow)
        setupFilterToggle(view, R.id.filter_employment_type, R.id.employment_type_header, R.id.employment_type_content, R.id.employment_type_arrow)
        setupFilterToggle(view, R.id.filter_contract_duration, R.id.contract_duration_header, R.id.contract_duration_content, R.id.contract_duration_arrow)
        setupFilterToggle(view, R.id.filter_work_schedule, R.id.work_schedule_header, R.id.work_schedule_content, R.id.work_schedule_arrow)
        setupFilterToggle(view, R.id.filter_salary, R.id.salary_header, R.id.salary_content, R.id.salary_arrow)
        setupFilterToggle(view, R.id.filter_accomodation, R.id.accomodation_header, R.id.accomodation_content, R.id.accomodation_arrow)
        setupFilterToggle(view, R.id.filter_education_level, R.id.education_level_header, R.id.education_level_content, R.id.education_level_arrow)
        setupFilterToggle(view, R.id.filter_language_requirements, R.id.language_requirements_header, R.id.language_requirements_content, R.id.language_requirements_arrow)
        setupFilterToggle(view, R.id.filter_internship_apprenticeship, R.id.internship_apprenticeship_header, R.id.internship_apprenticeship_content, R.id.internship_apprenticeship_arrow)
        setupFilterToggle(view, R.id.filter_industry, R.id.industry_header, R.id.industry_content, R.id.industry_arrow)
        setupFilterToggle(view, R.id.filter_company_size, R.id.company_size_header, R.id.company_size_content, R.id.company_size_arrow)
        setupFilterToggle(view, R.id.filter_company_rating, R.id.company_rating_header, R.id.company_rating_content, R.id.company_rating_arrow)
        setupFilterToggle(view, R.id.filter_remote_in_office, R.id.remote_in_office_header, R.id.remote_in_office_content, R.id.remote_in_office_arrow)
        setupFilterToggle(view, R.id.filter_date_posted, R.id.date_posted_header, R.id.date_posted_content, R.id.date_posted_arrow)
        setupFilterToggle(view, R.id.filter_benefits_offered, R.id.benefits_offered_header, R.id.benefits_offered_content, R.id.benefits_offered_arrow)

        return view
    }

    /**
     * Helper to setup toggle for a filter section.
     * Rotates arrow 0° (collapsed) → 90° (expanded).
     */
    private fun setupFilterToggle(
        rootView: View,
        filterLayoutId: Int,
        headerId: Int,
        contentId: Int,
        arrowId: Int
    ) {
        val filterLayout = rootView.findViewById<LinearLayout>(filterLayoutId)
        val header = rootView.findViewById<LinearLayout>(headerId)
        val content = rootView.findViewById<LinearLayout>(contentId)
        val arrow = rootView.findViewById<ImageView>(arrowId)

        header.setOnClickListener {
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            val isVisible = content.visibility == View.VISIBLE
            content.visibility = if (isVisible) View.GONE else View.VISIBLE
            arrow.animate().rotation(if (isVisible) 0f else 90f).setDuration(200).start()
        }
    }
}
