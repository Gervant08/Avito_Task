package com.gervant08.avitotask.ui.main

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.gervant08.avitotask.R
import com.gervant08.avitotask.model.data.Element
import com.gervant08.avitotask.model.tools.ElementItemAnimator


class MainActivity : AppCompatActivity()  {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory()}
    private  var adapter = MainAdapter()
    private  var recyclerView: RecyclerView? = null
    private  var deleteButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initRecycler()
        viewModel.generateNewElements() // Starting asynchronous adding of elements
    }

    private fun initViews(){
        deleteButton = findViewById(R.id.deleteButton)
    }

    private fun initRecycler(){
        adapter.setElements(viewModel.elementsList.value ?: arrayListOf())

        recyclerView = findViewById(R.id.mainRecycler)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.itemAnimator = ElementItemAnimator()
        recyclerView?.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        viewModel.elementsList.observe(this, this::onChanged)                       // Subscribe to changes in the main list of items
        MainViewModel.PoolOfDeletedItems.pool.observe(this, this::onDeleted)        // Subscribe to changes in the list of deleted items to remove them from the main list
    }

    private fun onChanged(elementsList: ArrayList<Element>?) {
        adapter.setElements(elementsList ?: arrayListOf())             // Passing a new list of elements to the adapter

        // Compare the length of the previous list with the current one. If the previous value is greater, then the element has been deleted.
        if (viewModel.previousListSize > elementsList!!.size)
            adapter.notifyItemRemoved(viewModel.deletedElementIndex)
        else if (viewModel.previousListSize < elementsList.size)  //If less, then the item was added to the list
            adapter.notifyItemInserted(viewModel.newElementIndex)

    }

    private fun onDeleted(pool: ArrayList<Element>){
        if (pool.isNotEmpty()) viewModel.deleteElement(pool) // If the list of deleted items is not empty, then run the delete function

    }

    override fun onDestroy() {
        recyclerView?.adapter = null
        recyclerView = null
        deleteButton = null

        super.onDestroy()
    }
}