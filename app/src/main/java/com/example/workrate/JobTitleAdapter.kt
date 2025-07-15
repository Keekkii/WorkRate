package com.example.workrate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workrate.data.JobTitle

class JobTitleAdapter : RecyclerView.Adapter<JobTitleAdapter.ViewHolder>() {

    private var jobTitles = listOf<JobTitle>()
    private val selectedItems = mutableSetOf<JobTitle>()

    // Callback for selection changes
    var onSelectionChanged: ((selectedCount: Int) -> Unit)? = null

    fun setFullList(list: List<JobTitle>) {
        jobTitles = list
        notifyDataSetChanged()
        // Notify initial selection state (probably zero)
        onSelectionChanged?.invoke(selectedItems.size)
    }

    fun getSelectedItems(): List<JobTitle> = selectedItems.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job_title, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = jobTitles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jobTitles[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.job_title_text)
        private val checkbox: ImageView = itemView.findViewById(R.id.job_checkbox_custom)

        fun bind(jobTitle: JobTitle) {
            titleText.text = jobTitle.title

            val isChecked = selectedItems.contains(jobTitle)
            checkbox.setImageResource(
                if (isChecked) R.drawable.ic_checkbox_checked
                else R.drawable.ic_checkbox_unchecked
            )

            itemView.setOnClickListener {
                val nowChecked = !selectedItems.contains(jobTitle)
                if (nowChecked) selectedItems.add(jobTitle)
                else selectedItems.remove(jobTitle)

                notifyItemChanged(adapterPosition)
                // Notify fragment about selection count change
                onSelectionChanged?.invoke(selectedItems.size)
            }
        }
    }
}
