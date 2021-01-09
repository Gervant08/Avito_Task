package com.gervant08.avitotask.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gervant08.avitotask.R
import com.gervant08.avitotask.model.data.Element

class MainAdapter: RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var elementsList: ArrayList<Element> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_element, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.onBind(elementsList[position])
    }

    override fun getItemCount(): Int = elementsList.size

    fun setElements(elementsList: ArrayList<Element>){
        this.elementsList = elementsList
        
    }


    class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private  val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        private  val elementId: TextView = itemView.findViewById(R.id.textId)

        fun onBind(element: Element){
            elementId.text = element.id.toString()
            deleteButton.setOnClickListener{deleteElement(element)}
        }

        private fun deleteElement(element: Element) {
            var isClone = false
            // When you click on the button, add this item to the list of deleted items
            MainViewModel.PoolOfDeletedItems.pool.apply {
                // If the item id is already in the pool, then don't add it
                // If you click on several delete buttons at the same time, several items with the same id will be added to the pool
                this.value?.forEach { if (it.id == element.id) isClone = true}

                if (!isClone){
                    this.value?.add(element)
                    MainViewModel.PoolOfDeletedItems.pool.value = this.value
                }

            }

        }


    }
}