package com.example.timernetic.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timernetic.R
import com.example.timernetic.ViewGroupActivity

class groupAdapter (private val groupList: List<groupData>) :RecyclerView.Adapter<groupAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_group_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groupList[position]
        holder.bind(group)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewgroupName: TextView = itemView.findViewById(R.id.textView3)

        init {
            // Add click listener to the item view
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val group = groupList[position]
                    // Pass the category name to the next activity
                    val intent = Intent(itemView.context, ViewGroupActivity::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }
        fun bind(group: groupData) {
            textViewgroupName.text = group.task


            // Load the category picture using your preferred method
            // imageViewCategoryPicture.loadImageFromUrl(category.categoryPicture)
        }
    }
}