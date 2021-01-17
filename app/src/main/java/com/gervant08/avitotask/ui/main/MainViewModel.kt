package com.gervant08.avitotask.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gervant08.avitotask.model.data.Element
import com.gervant08.avitotask.model.data.ElementsList
import com.gervant08.avitotask.model.data.PoolOfDeletedItems
import kotlinx.coroutines.*
import kotlin.random.Random


class MainViewModel : ViewModel() {

    private val _mutableElementList = MutableLiveData(ElementsList.list)
    val elementsList: LiveData<ArrayList<Element>> get() = _mutableElementList

    /* A variable that is used to cancel the launch coroutine, if one is already running*/
    private var coroutineIsRunning = false

    /* Variable that is needed to compare with the new list */
    var previousListSize = 0

    /* Variable used to store the index of the new item that was added to the list*/
    var newElementIndex = 0

    /* Variable used to store the index of a deleted item that was removed from the list*/
    var deletedElementIndex = 0

    fun deleteElement(pool: ArrayList<Element>) {
        /*  Remember the size of the list before deleting the item*/
        previousListSize = _mutableElementList.value!!.size
        /* We remember the index of the element we want to delete. The item came last in the list of deleted items*/
        deletedElementIndex = _mutableElementList.value!!.indexOf(pool.last())
        /* Deleting an element*/
        val newElementList = _mutableElementList.apply {
            this.value?.remove(pool.last())
        }
        _mutableElementList.value = newElementList.value
    }

    fun generateNewElements() {
        /* If one coroutine is already running, then exit the method
         without this condition, a new coroutine will start every time you turn the screen*/
        if (coroutineIsRunning) {
            return
        }

        viewModelScope.launch {
            while (true) {
                coroutineIsRunning = true
                delay(5_000)
                /* creating a new element*/
                val element = Element(createNewElementId())

                val newElementsList = _mutableElementList

                /*If the list is empty or there is only one element in it, then just add a new element and remember its index*/
                /*Random.nextInt() it doesn't work with an empty list or dimension = 1( nextInt(0, 0) )*/
                if (newElementsList.value?.isEmpty() == true || newElementsList.value?.size == 1) {
                    /* Remember the size before adding the element*/
                    previousListSize = newElementsList.value!!.size
                    /* Adding an element*/
                    newElementsList.value?.add(element)
                    /* Remember its index*/
                    newElementIndex = newElementsList.value!!.lastIndex
                } else {
                    /* A random position is created for the future element*/
                    val randomIndex = Random.nextInt(0, _mutableElementList.value!!.lastIndex)
                    /* Remember the size before adding the element*/
                    previousListSize = newElementsList.value!!.size
                    /* Adding an element to a random position*/
                    newElementsList.value?.add(randomIndex, element)
                    /* Remember its index*/
                    newElementIndex = randomIndex
                }

                _mutableElementList.value = newElementsList.value
            }
        }
    }


    private fun createNewElementId(): Int {

        val id: Int

        /*  If the pool of deleted items is empty, then just increase the item id*/
        if (PoolOfDeletedItems.pool.value!!.isEmpty())
            id = _mutableElementList.value?.maxByOrNull { it.id }!!.id + 1
        else {
            /* Otherwise, we take the id of the first element from the pool*/
            id = PoolOfDeletedItems.pool.value!!.first().id

            /* Removing this element from the pool*/
            val newPool = PoolOfDeletedItems.pool.apply {
                this.value?.remove(PoolOfDeletedItems.pool.value?.first())
            }
            PoolOfDeletedItems.pool.value = newPool.value
        }

        return id
    }

    fun addElementToPool(element: Element) {

        var isClone = false

        // When you click on the button, add this item to the list of deleted items
        PoolOfDeletedItems.pool.apply {
            // If the item id is already in the pool, then don't add it
            // If you click on several delete buttons at the same time, several items with the same id will be added to the pool
            this.value?.forEach { if (it.id == element.id) isClone = true }

            if (!isClone) {
                this.value?.add(element)
                PoolOfDeletedItems.pool.value = this.value
            }

        }
    }

    fun updateAdapterList(elementsList: ArrayList<Element>, adapter: MainAdapter) {

        adapter.setElementsList(elementsList)
        // Compare the length of the previous list with the current one. If the previous value is greater, then the element has been deleted.
        if (previousListSize > elementsList.size)
            adapter.notifyItemRemoved(deletedElementIndex)
        //If less, then the item was added to the list
        else if (previousListSize < elementsList.size)
            adapter.notifyItemInserted(newElementIndex)

    }

}

