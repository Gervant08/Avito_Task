package com.gervant08.avitotask.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gervant08.avitotask.R
import com.gervant08.avitotask.model.data.Element

class MainAdapter(private val listener: (Element) -> Unit) : ListAdapter<Element, MainAdapter.MainViewHolder>(ElementDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
            MainViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_element, parent, false))


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }


    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        private val elementId: TextView = itemView.findViewById(R.id.textId)

        fun onBind(element: Element) {
            elementId.text = element.id.toString()
            deleteButton.setOnClickListener { listener(element) }
        }

    }
}

private class ElementDiffCallback : DiffUtil.ItemCallback<Element>() {

    override fun areItemsTheSame(oldItem: Element, newItem: Element): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: Element, newItem: Element): Boolean {
        return oldItem == newItem
    }


}