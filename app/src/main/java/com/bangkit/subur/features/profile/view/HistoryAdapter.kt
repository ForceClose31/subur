package com.bangkit.subur.features.profile.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.subur.R
import com.bumptech.glide.Glide
import com.bangkit.subur.features.profile.data.DetectionHistory

class HistoryAdapter : ListAdapter<DetectionHistory, HistoryAdapter.HistoryViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<DetectionHistory>() {
        override fun areItemsTheSame(oldItem: DetectionHistory, newItem: DetectionHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DetectionHistory, newItem: DetectionHistory): Boolean {
            return oldItem == newItem
        }
    }

    class HistoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageViewHistory)
        private val textPredictedClass: TextView = view.findViewById(R.id.textPredictedClass)
        private val textConfidence: TextView = view.findViewById(R.id.textConfidence)
        private val textHandlingInstructions: TextView = view.findViewById(R.id.textHandlingInstructions)

        fun bind(history: DetectionHistory) {
            Glide.with(view.context)
                .load(history.imageUri)
                .centerCrop()
                .into(imageView)
            textPredictedClass.text = "Class: ${history.predictedClass}"
            textConfidence.text = "Confidence: ${history.confidence}"
            textHandlingInstructions.text = "Instructions: ${history.handlingInstructions}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
