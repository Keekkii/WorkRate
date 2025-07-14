package com.example.workrate

import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
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

        // Job Title filter (with style change)
        setupFilterToggleStyled(
            view,
            R.id.filter_job_title,
            R.id.job_title_header,
            R.id.job_title_content,
            R.id.job_title_arrow,
            R.id.job_title_text
        )

        // Other filters (simple toggle, no style change)
        setupFilterToggle(view, R.id.filter_location, R.id.location_header, R.id.location_content, R.id.location_arrow)
        // ... other filters here ...

        // Force set checkbox drawable for ALL checkboxes inside job_title_content
        val jobTitleContent = view.findViewById<LinearLayout>(R.id.job_title_content)
        setCustomCheckboxDrawables(jobTitleContent)

        return view
    }

    // Recursively find all AppCompatCheckBox children and set button drawable & tint
    private fun setCustomCheckboxDrawables(parent: ViewGroup) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is AppCompatCheckBox) {
                // Set your selector drawable programmatically
                child.buttonDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.checkbox_selector)
                // Remove any button tint to avoid tint override
                child.buttonTintList = null
            } else if (child is ViewGroup) {
                // Recursive call if the child is also a ViewGroup
                setCustomCheckboxDrawables(child)
            }
        }
    }

    // For Job Title: style-changing toggle
    private fun setupFilterToggleStyled(
        rootView: View,
        filterLayoutId: Int,
        headerId: Int,
        contentId: Int,
        arrowId: Int,
        headerTextId: Int
    ) {
        val filterLayout = rootView.findViewById<LinearLayout>(filterLayoutId)
        val header = rootView.findViewById<LinearLayout>(headerId)
        val content = rootView.findViewById<LinearLayout>(contentId)
        val arrow = rootView.findViewById<ImageView>(arrowId)
        val headerText = rootView.findViewById<TextView>(headerTextId)

        header.setOnClickListener {
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            val isVisible = content.visibility == View.VISIBLE
            content.visibility = if (isVisible) View.GONE else View.VISIBLE
            arrow.animate().rotation(if (isVisible) 0f else 90f).setDuration(200).start()
            header.isSelected = !isVisible
            val colorExpanded = Color.WHITE
            val colorCollapsed = Color.parseColor("#1156AC")
            headerText.setTextColor(if (!isVisible) colorExpanded else colorCollapsed)
            arrow.setColorFilter(if (!isVisible) colorExpanded else colorCollapsed)
        }
    }

    // For all other filters: simple toggle
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
