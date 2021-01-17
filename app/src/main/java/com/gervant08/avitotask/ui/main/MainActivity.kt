package com.gervant08.avitotask.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.gervant08.avitotask.R
import com.gervant08.avitotask.model.data.Element
import com.gervant08.avitotask.model.data.PoolOfDeletedItems
import com.gervant08.avitotask.model.tools.ElementItemAnimator

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory() }
    private val adapter = MainAdapter { element -> viewModel.addElementToPool(element) }
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecycler()
        /* Subscribe to changes in the main list of items*/
        viewModel.elementsList.observe(this, this::onChanged)
        /*  Subscribe to changes in the list of deleted items to remove them from the main list*/
        PoolOfDeletedItems.pool.observe(this, this::onDeleted)
        /*  Starting asynchronous adding of elements*/
        viewModel.generateNewElements()
    }

    private fun initRecycler() {

        recyclerView = findViewById(R.id.mainRecycler)

        with(recyclerView) {
            setHasFixedSize(true)
            itemAnimator = ElementItemAnimator()
            adapter = this@MainActivity.adapter
        }
    }

    private fun onChanged(elementsList: ArrayList<Element>?) {
        // Passing a new list of elements to the adapter
        adapter.submitList(elementsList)

        // Compare the length of the previous list with the current one. If the previous value is greater, then the element has been deleted.
        if (viewModel.previousListSize > elementsList!!.size)
            adapter.notifyItemRemoved(viewModel.deletedElementIndex)
        //If less, then the item was added to the list
        else if (viewModel.previousListSize < elementsList.size)
            adapter.notifyItemInserted(viewModel.newElementIndex)

    }

    private fun onDeleted(pool: ArrayList<Element>) {
        /* If the list of deleted items is not empty, then run the delete function*/
        if (pool.isNotEmpty()) viewModel.deleteElement(pool)
    }
}