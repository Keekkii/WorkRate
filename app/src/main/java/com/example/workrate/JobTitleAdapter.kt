package com.example.workrate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workrate.data.JobTitle

class JobTitleAdapter : RecyclerView.Adapter<JobTitleAdapter.ViewHolder>() {

    private var jobTitles = listOf<JobTitle>()
    private val selectedItems = mutableSetOf<JobTitle>()

    fun setFullList(list: List<JobTitle>) {
        jobTitles = list
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<JobTitle> = selectedItems.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job_title, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = jobTitles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jobTitle = jobTitles[position]
        holder.bind(jobTitle)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.job_title_text)
        private val checkBox: CheckBox = itemView.findViewById(R.id.job_checkbox)

        fun bind(jobTitle: JobTitle) {
            titleText.text = jobTitle.title

            // Remove listener to avoid triggering it on recycle
            checkBox.setOnCheckedChangeListener(null)

            // Set current state
            checkBox.isChecked = selectedItems.contains(jobTitle)

            // Reattach listener
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItems.add(jobTitle)
                } else {
                    selectedItems.remove(jobTitle)
                }
            }

            // Optional: click row to toggle checkbox
            itemView.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
            }
        }
    }
}
