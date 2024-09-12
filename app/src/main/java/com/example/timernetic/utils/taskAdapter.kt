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
import com.example.timernetic.utils.ImageEncoder.decodeImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class taskAdapter : RecyclerView.Adapter<taskAdapter.ViewHolder>() {
    private val taskDataList: MutableList<taskData> = mutableListOf()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_task_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskData = taskDataList[position]
        holder.bind(taskData)
    }

    override fun getItemCount(): Int {
        return taskDataList.size
    }
    // Inner ViewHolder class
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskNameTextView: TextView = itemView.findViewById(R.id.taskName)
        private val taskDescriptionTextView: TextView = itemView.findViewById(R.id.taskDescription)
        private val taskStartTextView: TextView = itemView.findViewById(R.id.taskStartDate)
        private val taskEndTextView: TextView = itemView.findViewById(R.id.taskEndDate)
        private val TaskPictureImageView: ImageView = itemView.findViewById(R.id.TaskPictureImageView)

        fun bind(taskData: taskData) {
            taskNameTextView.text = taskData.taskName
            taskDescriptionTextView.text = taskData.taskDescription
            taskStartTextView.text = taskData.taskStart
            taskEndTextView.text = taskData.taskEnd
            if (taskData.taskPicture!=null)
            {
                TaskPictureImageView.setImageBitmap(decodeImage(taskData.taskPicture))
                TaskPictureImageView.visibility = View.VISIBLE
            }else{
                TaskPictureImageView.visibility = View.GONE
            }

        }
    }
    // Function to set the dataset
    fun setTaskData(data: List<taskData>) {
        taskDataList.clear()
        taskDataList.addAll(data)
        notifyDataSetChanged()
    }
}
